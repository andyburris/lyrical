package com.andb.apps.lyrical.pages.dataexplorer.data

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawAudioHistoryEntry(
    @SerialName("ts") val timestamp: String,
    val username: String,
    val platform: String,
    @SerialName("ms_played") val millisecondsPlayed: Long,
    @SerialName("conn_country") val connectionCountry: String,
    @SerialName("ip_addr_decrypted") val ipAddress: String?,
    @SerialName("user_agent_decrypted") val userAgent: String?,
    @SerialName("master_metadata_track_name") val trackName: String?,
    @SerialName("master_metadata_album_artist_name") val trackArtist: String?,
    @SerialName("master_metadata_album_album_name") val trackAlbum: String?,
    @SerialName("spotify_track_uri") val trackURI: String?,
    @SerialName("episode_name") val episodeName: String?,
    @SerialName("episode_show_name") val episodeShowName: String?,
    @SerialName("spotify_episode_uri") val episodeURI: String?,
    @SerialName("reason_start") val startReason: String?,
    @SerialName("reason_end") val endReason: String?,
    val shuffle: Boolean?,
    val skipped: Boolean?,
    val offline: Boolean?,
    @SerialName("offline_timestamp") val offlineTimestamp: Long?,
    @SerialName("incognito_mode") val incognitoMode: Boolean,
)

@Serializable
data class AudioHistoryEntry(
    val timestamp: Instant,
    val platform: String,
    val millisecondsPlayed: Long,
    val trackName: String,
    val artistName: String,
    val uri: String,
    val startReason: StartReason,
    val endReason: EndReason,
    val shuffle: Boolean?,
    val skipped: Boolean?,
    val offline: Boolean?,
)

external interface SavedAudioHistoryEntry {
    var uuid: String
    var timestamp: String
    var platform: String
    var millisecondsPlayed: String
    var trackName: String
    var artistName: String
    var uri: String
    var startReason: String
    var endReason: String
    var shuffle: String
    var skipped: String
    var offline: String
}

fun SavedAudioHistoryEntry.toAudioHistoryEntry() = AudioHistoryEntry(Instant.parse(timestamp), platform, millisecondsPlayed.toLong(), trackName, artistName, uri, StartReason.valueOf(startReason), EndReason.valueOf(endReason), shuffle.toBooleanStrictOrNull(), skipped.toBooleanStrictOrNull(), offline.toBooleanStrictOrNull())

enum class StartReason {
    AppLoad,
    BackButton,
    ClickRow,
    ForwardButton,
    Persisted,
    PlayButton,
    Remote,
    TrackDone,
    TrackError,
    Unknown,
}

enum class EndReason {
    BackButton,
    EndPlay,
    ForwardButton,
    Logout,
    PlayButton,
    Remote,
    TrackDone,
    TrackError,
    UnexpectedExit,
    UnexpectedExitWhilePaused,
    Unknown,
}