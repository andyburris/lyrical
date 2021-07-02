package com.andb.apps.lyricalbackend

import GameConfig
import GenericTrack
import User
import kotlinx.serialization.Serializable

@Serializable
sealed class ServerAction {
    @Serializable
    sealed class Lobby : ServerAction() {
        @Serializable object SendKicked : Lobby()
        @Serializable data class UpdateJoined(val users: List<User>) : Lobby()
        @Serializable data class OptionsUpdated(val config: GameConfig) : Lobby()
    }
    @Serializable data class SendAnswer(val track: GenericTrack)
}