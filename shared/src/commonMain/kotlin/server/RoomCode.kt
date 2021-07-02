package server

typealias RoomCode = String

fun String.isValidRoomCode() = this.length == 6 && this.all { it in '0'..'9' || it in 'a'..'z' || it in 'A'..'Z' }