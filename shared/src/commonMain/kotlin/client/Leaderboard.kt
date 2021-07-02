package client

import User
import kotlinx.serialization.Serializable

@Serializable
data class Leaderboard(val players: List<LeaderboardPlayer>)

@Serializable
data class LeaderboardPlayer(val user: User, val points: Double, val questionsAnswered: Int, val connected: Boolean)
