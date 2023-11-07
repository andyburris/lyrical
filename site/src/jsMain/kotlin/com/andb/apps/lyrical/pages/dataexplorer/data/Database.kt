package com.andb.apps.lyrical.pages.dataexplorer.data

import com.juul.indexeddb.Database
import com.juul.indexeddb.EventHandlerException
import com.juul.indexeddb.KeyPath
import com.juul.indexeddb.openDatabase
import js.core.jso
import web.crypto.crypto
import web.window.window

const val SpotifyDataExplorerDatabase = "spotify-data-explorer"
const val SpotifyDataExplorerStore = "spotify-data-explorer-store"
suspend fun initializeDatabase(): Database {
    val database = openDatabase(SpotifyDataExplorerDatabase, 1) { database, oldVersion, newVersion ->
        println("migrating from $oldVersion to $newVersion")
        if (oldVersion < 1) {
            val store = database.createObjectStore(SpotifyDataExplorerStore, KeyPath("uuid"))
            listOf("timestamp", "trackName", "uri", "artistName", "millisecondsPlayed", "startReason", "endReason", "shuffle", "skipped", "offline", "platform").forEach {
                store.createIndex(it, KeyPath(it), unique = false)
            }
        }
    }
    return database
}

suspend fun Database.loadEntries(): List<AudioHistoryEntry> {
    val loadedEntries = this.transaction(SpotifyDataExplorerStore) {
        objectStore(SpotifyDataExplorerStore).getAll() as Array<SavedAudioHistoryEntry>
    }
    return loadedEntries.map { it.toAudioHistoryEntry() }
}

suspend fun Database.clearEntries() {
    this.writeTransaction(SpotifyDataExplorerStore) {
        val store = objectStore(SpotifyDataExplorerStore)
        store.clear()
    }
}

suspend fun Database.saveEntries(entries: List<AudioHistoryEntry>) {
    this.writeTransaction(SpotifyDataExplorerStore) {
        val store = objectStore(SpotifyDataExplorerStore)
        val savableEntries: List<SavedAudioHistoryEntry> = entries.map { entry ->
            val timestampString = entry.timestamp.toString()
            val jsObject = jso<SavedAudioHistoryEntry> {
                uuid = crypto.randomUUID()
                timestamp = timestampString
                platform = entry.platform
                millisecondsPlayed = entry.millisecondsPlayed.toString()
                trackName = entry.trackName
                artistName = entry.artistName
                uri = entry.uri
                startReason = entry.startReason.name
                endReason = entry.endReason.name
                shuffle = entry.shuffle.toString()
                skipped = entry.skipped.toString()
                offline = entry.offline.toString()
            }
            return@map jsObject
        }
//            .filter { it.timestamp !in listOf("2016-02-18T01:48:41", "2017-04-23T16:57:10") }
        savableEntries.forEachIndexed { index, entry ->
            try {
                store.add(entry)
            } catch (e: EventHandlerException) {
                println("caught exception adding jsObject to store (entry $index)")
                val event = e.event
                println("event = ${JSON.stringify(event, arrayOf("message", "arguments", "type", "name"))}")
            }
        }
    }
}