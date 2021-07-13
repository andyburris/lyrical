package com.andb.apps.lyricalbackend

import java.io.File
import spotifyClientID

object Keys {
    val geniusAccessToken: String = getEnvironmentalVariable("geniusAccessToken")
    object Spotify {
        val clientSecret: String = getEnvironmentalVariable("spotifyClientSecret")
        val clientID: String = spotifyClientID
    }
    object JWT {
        val audience = getEnvironmentalVariable("jwtAudience")
        val realm = getEnvironmentalVariable("jwtRealm")
        val secret = getEnvironmentalVariable("jwtSecret")
        val domain = getEnvironmentalVariable("jwtDomain")
    }
}

private fun getEnvironmentalVariable(key: String) = System.getenv()[key] ?: File("../local.properties").readText().lines().first { it.startsWith(key) }.takeLastWhile { it != '=' }