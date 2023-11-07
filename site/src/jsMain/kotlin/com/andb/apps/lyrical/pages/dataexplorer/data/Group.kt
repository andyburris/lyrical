package com.andb.apps.lyrical.pages.dataexplorer.data

import kotlinx.datetime.LocalDateTime

data class Group(
    val type: GroupType,
    val dateTime: LocalDateTime,
    val items: List<Combination>
)

sealed class Combination {
    abstract val listens: List<AudioHistoryEntry>

    data class Track(
        val name: String,
        val artist: String,
        override val listens: List<AudioHistoryEntry>
    ) : Combination()

    data class Artist(
        val name: String,
        override val listens: List<AudioHistoryEntry>,
    ) : Combination()
}