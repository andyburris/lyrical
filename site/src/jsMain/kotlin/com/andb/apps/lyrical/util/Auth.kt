package com.andb.apps.lyrical.util

import AuthAction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.core.rememberPageContext


@Composable
fun handleAuth(onCheckAuth: (AuthAction.CheckAuthentication) -> Unit) {
    val context = rememberPageContext()
    if (context.route.fragment?.startsWith("access_token") == true) {
        val params = context.route.fragment
            ?.split("&")
            ?.map { it.takeLastWhile { it != '=' } }
            ?: emptyList()
        if (params.size >= 4) {
            val (token, tokenType, expiresIn, state) = params
            val action = AuthAction.CheckAuthentication(token, tokenType, expiresIn.toInt(), state)
            onCheckAuth(action)
            context.router.navigateTo("/")
        }
    }
}