package model

import GameConfig
import GenericArtist
import GenericTrack
import SpotifyRepository
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.SimpleArtist
import com.adamratzman.spotify.models.SimplePlaylist
import distributeInto
import getGenericPlaylistTracks
import kotlinx.serialization.Serializable
import toGenericArtist
import toGenericTrack

@Serializable
sealed class GenericPlaylist {
    abstract val trackCount: Int
    @Serializable data class Playlist(
        val id: String,
        val name: String,
        val user: String,
        val imageURL: String?,
        override val trackCount: Int
    ) : GenericPlaylist()
    @Serializable data class Artist(
        val artist: GenericArtist,
        override val trackCount: Int
    ) : GenericPlaylist()
}


suspend fun List<GenericPlaylist>.getRandomSongs(spotifyRepository: SpotifyRepository, config: GameConfig): List<Pair<GenericTrack, GenericPlaylist>> {
    println("getting random songs, config = $config")
    return when(config.distributePlaylistsEvenly) {
        true -> {
            val amountOfSongsPerPlaylist = config.amountOfSongs.distributeInto(this.size)
            return this.getEvenlyDistributedRandomSongs(spotifyRepository, amountOfSongsPerPlaylist)
        }
        false -> this.getRandomSongs(spotifyRepository, config.amountOfSongs, emptyList())
    }
}

private suspend fun List<GenericPlaylist>.getEvenlyDistributedRandomSongs(spotifyRepository: SpotifyRepository, distribution: List<Int>): List<Pair<GenericTrack, GenericPlaylist>> = this
    .zip(distribution)
    .fold(emptyList()) { tracks, (playlist, amountOfSongs) ->
        tracks + listOf(playlist).getRandomSongs(spotifyRepository, amountOfSongs, tracks.map { it.first })
    }

private suspend fun List<GenericPlaylist>.getRandomSongs(spotifyRepository: SpotifyRepository, amountOfSongs: Int, ineligibleTracks: List<GenericTrack>): List<Pair<GenericTrack, GenericPlaylist>> = this
    .flatMap { playlist -> spotifyRepository.getGenericPlaylistTracks(playlist).map { it.toGenericTrack() to playlist } }
    .distinctBy { it.first.id }
    .filter { it.first !in ineligibleTracks }
    .shuffled()
    .take(amountOfSongs)

fun SimplePlaylist.toGenericPlaylist() = GenericPlaylist.Playlist(this.uri.uri, this.name, this.owner.displayName ?: "Anonymous", this.images.firstOrNull()?.url, this.tracks.total)
//fun Artist.toGenericPlaylist() = GenericPlaylist.Artist(this.toGenericArtist(), ) TODO: add artist as a playlist type