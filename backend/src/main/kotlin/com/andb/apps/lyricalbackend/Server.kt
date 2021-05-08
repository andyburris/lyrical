import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    val geniusAccessToken: String = System.getenv()["geniusAccessToken"] ?: File("../local.properties").readText().takeLastWhile { it != '=' }
    val geniusRepository = GeniusRepository(geniusAccessToken)
    embeddedServer(Netty, port = System.getenv()["PORT"]?.toInt() ?: 5050) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            method(HttpMethod.Options)
            header("lyricRequests")
            anyHost()
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
        }
    }.start(true)
}