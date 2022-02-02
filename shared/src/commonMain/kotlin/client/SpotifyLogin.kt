package client

import SpotifyRepository
import com.adamratzman.spotify.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import spotifyClientID



expect fun openSpotifyLogin()
expect fun openURLInBrowserTab(url: String)
expect fun spotifyRepository(defaultRepository: SpotifyRepository): StateFlow<SpotifyRepository>