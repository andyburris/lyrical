import client.ClientResponse
import client.serialize
import com.adamratzman.spotify.spotifyAppApi
import com.andb.apps.lyricalbackend.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.json.Json
import server.RoomCode
import server.RoomMachine
import server.ServerError
import java.util.*

fun main() {
    val geniusRepository = GeniusRepository(Keys.geniusAccessToken)
    val roomRepository = MemoryRoomRepository()
    val api = runBlocking { spotifyAppApi(Keys.Spotify.clientID, Keys.Spotify.clientSecret).build() }
    val spotifyRepository = LocalSpotifyRepository(api)
    val lyricsRepository = LyricsRepository()
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    embeddedServer(Netty, port = System.getenv()["PORT"]?.toInt() ?: 5050, host = "localhost") {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            method(HttpMethod.Options)
            header("lyricRequests")
            anyHost()
        }
        install(WebSockets)
        install(Authentication) {
            jwt() {
                realm = Keys.JWT.realm
                verifier(jwtAccessVerifier())
                validate { credential ->
                    if (Keys.JWT.audience in credential.payload.audience) JWTPrincipal(credential.payload) else null
                }
            }
            jwt("refresh") {
                realm = Keys.JWT.realm
                verifier(jwtRefreshVerifier())
                validate { credential ->
                    if (Keys.JWT.audience in credential.payload.audience) JWTPrincipal(credential.payload) else null
                }
            }
        }

        routing {
            get("/lyrics") {
                println("recieved request from ${call.request.local.let { it.host + ":" + it.port + it.uri }}")
                val requests = (call.request.header("lyricRequests") ?: throw Error("Must have a lyricRequests header")).decodeLyricRequestsFromString()
                println("recieved request, tracks = $requests")
                val lyrics = requests.map { request ->
                    CoroutineScope(Dispatchers.IO).async {
                        println("getting lyrics for request = $request")
                        val lyrics = geniusRepository.getLyrics(request.name, request.artists)?.lines()
                        println("got lyrics for request = $request, lyrics = $lyrics")
                        LyricResponse(request.trackID, lyrics)
                    }
                }
                val allLyrics = lyrics.awaitAll()
                println("allLyrics successful")
                call.respond(allLyrics)
            }
            route("/auth") {
                get("/anonymous") {
                    println("/auth/anonymous called")
                    val anonymousUserID = UUID.randomUUID().toString()
                    val accessJWT = generateJWT(anonymousUserID, isAccessToken = true)
                    val refreshJWT = generateJWT(anonymousUserID, isAccessToken = false)
                    call.respond(Pair(accessJWT, refreshJWT))
                }
                authenticate("refresh") {
                    get("/refresh") {
                        println("/auth/refresh called")
                        val savedUserID = call.authentication.principal<JWTPrincipal>()?.get("userID") ?: return@get
                        val newAccessJWT = generateJWT(savedUserID, isAccessToken = true)
                        val newRefreshJWT = generateJWT(savedUserID, isAccessToken = false)
                        call.respond(Pair(newAccessJWT, newRefreshJWT))
                    }
                }
            }

            authenticate() {
                post<RoomCode>("/creategame") {
                    println("/creategame called")
                    val user = call.authentication.principal<JWTPrincipal>()?.toUser() ?: return@post
                    val code = roomRepository.generateCode()
                    val roomMachine = RoomMachine(code, user, spotifyRepository, lyricsRepository, coroutineScope)
                    roomRepository.addRoom(roomMachine)
                    call.respond(roomMachine.code)
                }
                webSocket("/play/{gameCode}") {
                    println("/play called")
                    val code = call.parameters["gameCode"]!!
                    println("/play/$code opened")
                    val user = call.authentication.principal<JWTPrincipal>()?.toUser() ?: return@webSocket
                    println("authenticated, user = $user")
                    val roomMachine = roomRepository.connectToRoom(code, user)
                    if (roomMachine == null) {
                        //outgoing.send(Frame.Text(ClientResponse.Error(ServerError.InvalidCode).serialize()))
                        return@webSocket
                    }
                    println("roomMachine = $roomMachine")
                    this.launch {
                        roomMachine.responses.collect {
                            println("sending $it")
                            outgoing.send(Frame.Text(it.serialize()))
                        }
                    }
                    for (frame in incoming) {
                        println("received frame = ${(frame as? Frame.Text)?.readText()}")
                        when(frame) {
                            is Frame.Text -> {
                                val action = Json.decodeFromString(UserAction.serializer(), frame.readText())
                                roomMachine.handleAction(action)
                            }
                            is Frame.Close -> {

                            }
                        }
                    }
                }
            }

            remoteSpotifyRepository(spotifyRepository)
        }
    }.start(true)

}

fun Routing.remoteSpotifyRepository(localRepository: LocalSpotifyRepository) {
    get("/featured") { call.respond(localRepository.getFeaturedPlaylists()) }
    get("/playlistTracks/{uri}") { call.respond(localRepository.getPlaylistTracks(call.parameters["uri"]!!)) }
    get("/artistTracks/{uri}") { call.respond(localRepository.getArtistTracks(call.parameters["uri"]!!)) }
    get("/playlist/{uri}") { call.respond(localRepository.getPlaylistByURI(call.parameters["uri"]!!)) }
    get("/search/{query}") { call.respond(localRepository.searchPlaylists(call.parameters["query"]!!)) }
}

