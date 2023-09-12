package com.andb.apps.lyrical.util

import com.adamratzman.spotify.models.SimpleAlbum
import com.adamratzman.spotify.models.SimpleArtist

const val albumPlaceholderUrl = ""
const val playlistPlaceholderUrl = ""
fun SimpleAlbum.imageOrPlaceholder(): String {
    return this.images.firstOrNull()?.url ?: ""
}

fun List<SimpleArtist>.artistsToString(): String = this
    .map { it.name }
    .joinToString(", ")