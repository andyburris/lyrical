package com.andb.apps.lyricalbackend

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.days
import kotlin.time.minutes


@OptIn(ExperimentalTime::class)
private fun jwtVerifier() = JWT.require(Algorithm.HMAC256(Keys.JWT.secret))
    .withAudience(Keys.JWT.audience)
    .withIssuer(Keys.JWT.domain)
    .acceptLeeway(1.minutes.inWholeSeconds)

fun jwtAccessVerifier() = jwtVerifier()
    .withClaim("isAccessToken", true)
    .build()

fun jwtRefreshVerifier() = jwtVerifier()
    .withClaim("isAccessToken", false)
    .build()

fun generateJWT(userID: String, isAccessToken: Boolean): String = JWT.create()
    .withSubject("Authentication")
    .withIssuer(Keys.JWT.domain)
    .withAudience(Keys.JWT.audience)
    .withClaim("userID", userID)
    .withClaim("isAnonymous", true)
    .withClaim("isAccessToken", isAccessToken)
    .withExpiresAt(if (isAccessToken) getAccessTokenExpirationDate() else getRefreshTokenExpirationDate())
    .sign(Algorithm.HMAC256(Keys.JWT.secret))

@OptIn(ExperimentalTime::class) private val accessTokenExpirationMillis = 10.minutes.inWholeMilliseconds
@OptIn(ExperimentalTime::class) private val refreshTokenExpirationMillis = 30.days.inWholeMilliseconds
private fun getAccessTokenExpirationDate() = Date(System.currentTimeMillis() + accessTokenExpirationMillis)
private fun getRefreshTokenExpirationDate() = Date(System.currentTimeMillis() + refreshTokenExpirationMillis)