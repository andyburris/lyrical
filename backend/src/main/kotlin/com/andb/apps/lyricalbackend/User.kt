package com.andb.apps.lyricalbackend

import io.ktor.auth.jwt.*

fun JWTPrincipal.toUser() = when(this.getClaim("isAnonymous", Boolean::class)!!) {
    true -> User.Anonymous(this["userID"]!!)
    false -> User.Spotify(this["userID"]!!)
}
