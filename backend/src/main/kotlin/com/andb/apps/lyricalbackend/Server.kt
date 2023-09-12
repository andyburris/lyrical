package com.andb.apps.lyricalbackend

import GeniusRepository
import LyricResponse
import decodeLyricRequestsFromString
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import java.io.File

fun main() {
    embeddedServer(
        Netty,
        port = System.getenv()["PORT"]?.toInt() ?: 5050,
        host = "0.0.0.0",
        module = Application::applicationModule
    ).start(true)
}

fun Application.applicationModule() {
    val geniusRepository = GeniusRepository(getGeniusAccessToken())

    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowHeader("lyricRequests")
        anyHost()
    }

    routing {
        get("/lyrics") {
            val requests = (call.request.header("lyricRequests")
                ?: throw Error("Must have a lyricRequests header")).decodeLyricRequestsFromString()
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
            call.respond(allLyrics)
        }
    }
}

private fun getGeniusAccessToken(): String = System.getenv()["geniusAccessToken"]
    ?: File("../local.properties")
        .let { if (it.exists()) it else null }
        ?.readLines()
        ?.first { it.startsWith("geniusAccessToken") }
        ?.takeLastWhile { it != '=' }
    ?: throw Error("geniusAccessToken must exist as an environmental variable or in a local.properties file in the project root")