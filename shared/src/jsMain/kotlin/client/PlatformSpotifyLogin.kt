package client

import SpotifyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual fun openSpotifyLogin() {

}

actual fun openURLInBrowserTab(url: String) {

}

actual fun spotifyRepository(defaultRepository: SpotifyRepository): StateFlow<SpotifyRepository> = MutableStateFlow(defaultRepository)