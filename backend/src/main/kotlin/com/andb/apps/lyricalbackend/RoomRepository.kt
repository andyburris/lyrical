package com.andb.apps.lyricalbackend

import User
import server.RoomCode
import server.RoomMachine
import server.UserRoomMachine

interface RoomRepository {
    fun addRoom(machine: RoomMachine)
    fun connectToRoom(code: String, user: User): UserRoomMachine?
    fun generateCode(): RoomCode
}

class MemoryRoomRepository() : RoomRepository {
    private val rooms = mutableMapOf<RoomCode, RoomMachine>()

    override fun addRoom(machine: RoomMachine) {
        rooms += machine.code to machine
    }

    override fun connectToRoom(code: String, user: User): UserRoomMachine? {
        val roomMachine = rooms[code.uppercase()] ?: return null
        return UserRoomMachine(roomMachine, user)
    }

    override fun generateCode(): RoomCode {
        var random: String = randomString(length = 6)
        while (random in rooms) {
            random = randomString(length = 6)
        }
        return random
    }
}

private val randomSourceChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
fun randomString(length: Int, source: String = randomSourceChars) = (0 until length).map { source.random() }.joinToString("")
