package model

import GenericTrack
import kotlinx.serialization.Serializable

@Serializable
data class GenericGameTrack(val track: GenericTrack, val sourcePlaylist: GenericPlaylist, val lyrics: List<String>)

fun GenericTrack.toGameTrack(sourcePlaylist: GenericPlaylist, lyrics: List<String>) = GenericGameTrack(this, sourcePlaylist, lyrics)