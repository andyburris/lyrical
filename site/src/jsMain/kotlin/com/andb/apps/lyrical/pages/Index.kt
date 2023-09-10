package com.andb.apps.lyrical.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.core.Page
import com.andb.apps.lyrical.pages.home.HomePage
import com.varabyte.kobweb.core.rememberPageContext

@Page
@Composable
fun IndexPage() {
    HomePage()
}
