package com.andb.apps.lyrical.pages.dataexplorer.data

import js.core.Object
import js.typedarrays.Uint8Array
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import web.encoding.TextDecoder

fun ByteArray.toAudioHistoryEntries(): List<AudioHistoryEntry> {
    js(UZIPJsCode)
    val zipFile = UZIP.parse(this)
    val map = Object.entries(zipFile).asList().associate { (k, v) -> k to (v as Uint8Array) }
    return map.toAudioHistoryEntries()
}

fun Map<String, Uint8Array>.toAudioHistoryEntries(): List<AudioHistoryEntry> {
    val relevantFiles = this
        .filter { (name, _) -> name.endsWith(".json") && name.contains("Audio") }
    val entries = relevantFiles
        .flatMap { (_, file) ->
            val asString = TextDecoder().decode(file)
            Json.decodeFromString<List<RawAudioHistoryEntry>>(asString)
        }
        .mapNotNull { it.toAudioHistoryEntry() }
        .sortedBy { it.timestamp }
//        .takeLast(100)
    return entries
}

fun RawAudioHistoryEntry.toAudioHistoryEntry() = when {
    (this.trackName == null || this.trackArtist == null || this.trackURI == null) -> null
    else -> AudioHistoryEntry(
        timestamp = Instant.parse(timestamp),
        platform = platform,
        millisecondsPlayed = millisecondsPlayed,
        trackName = trackName,
        artistName = trackArtist,
        uri = trackURI,
        startReason = startReason.toStartReason(),
        endReason = endReason.toEndReason(),
        shuffle = shuffle,
        skipped = skipped,
        offline = offline,
    )
}

fun String?.toStartReason() = when(this) {
    "appload" -> StartReason.AppLoad
    "backbtn" -> StartReason.BackButton
    "clickrow" -> StartReason.ClickRow
    "fwdbtn" -> StartReason.ForwardButton
    "persisted" -> StartReason.Persisted
    "playbtn" -> StartReason.PlayButton
    "remote" -> StartReason.Remote
    "trackdone" -> StartReason.TrackDone
    "trackerror" -> StartReason.TrackError
    "unknown" -> StartReason.Unknown
    else -> StartReason.Unknown
}

fun String?.toEndReason() = when(this) {
    "backbtn" -> EndReason.BackButton
    "endplay" -> EndReason.EndPlay
    "fwdbtn" -> EndReason.ForwardButton
    "logout" -> EndReason.Logout
    "playbtn" -> EndReason.PlayButton
    "remote" -> EndReason.Remote
    "trackdone" -> EndReason.TrackDone
    "trackerror" -> EndReason.TrackError
    "unexpected-exit" -> EndReason.UnexpectedExit
    "unexpected-exit-while-paused" -> EndReason.UnexpectedExitWhilePaused
    "unknown" -> EndReason.Unknown
    else -> EndReason.Unknown
}