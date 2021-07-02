import client.serialize
import com.adamratzman.spotify.spotifyAppApi
import com.andb.apps.lyricalbackend.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
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
import java.util.*
import kotlin.time.*

fun main() {
    val geniusRepository = GeniusRepository(Keys.geniusAccessToken)
    val roomRepository = MemoryRoomRepository()
    val api = runBlocking { spotifyAppApi(Keys.Spotify.clientID, Keys.Spotify.clientSecret).build() }
    val spotifyRepository = LocalSpotifyRepository(api)
    val lyricsRepository = LyricsRepository()
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    embeddedServer(Netty, port = System.getenv()["PORT"]?.toInt() ?: 5050) {
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
                    val anonymousUserID = UUID.randomUUID().toString()
                    val accessToken = generateToken(anonymousUserID, isAccessToken = true)
                    val refreshToken = generateToken(anonymousUserID, isAccessToken = false)
                    call.respond(Pair(accessToken, refreshToken))
                }
                authenticate("refresh") {
                    get("/refresh") {
                        val savedUserID = call.authentication.principal<JWTPrincipal>()?.get("userID") ?: return@get
                        val newAccessToken = generateToken(savedUserID, isAccessToken = true)
                        val newRefreshToken = generateToken(savedUserID, isAccessToken = false)
                        call.respond(Pair(newAccessToken, newRefreshToken))
                    }
                }
            }

            authenticate {
                post<RoomCode>("/creategame") {
                    val user = call.authentication.principal<JWTPrincipal>()?.toUser() ?: return@post
                    val code = roomRepository.generateCode()
                    val roomMachine = RoomMachine(code, user, spotifyRepository, lyricsRepository, coroutineScope)
                    roomRepository.addRoom(roomMachine)
                    call.respond(roomMachine.code)
                }
                webSocket("/play/{gameCode}") {
                    val code = call.parameters["gameCode"]!!
                    val user = call.authentication.principal<JWTPrincipal>()?.toUser() ?: return@webSocket
                    val roomMachine = roomRepository.connectToRoom(code, user)
                    for (frame in incoming) {
                        when(frame) {
                            is Frame.Text -> {
                                val action = Json.decodeFromString(UserAction.serializer(), frame.readText())
                                roomMachine.handleAction(action)
                            }
                            is Frame.Close -> {

                            }
                        }
                    }
                    roomMachine.responses.collect {
                        outgoing.send(Frame.Text(it.serialize()))
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

