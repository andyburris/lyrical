package server

import GameAction
import GameScreen
import LobbyAction
import User
import UserAction
import answeredQuestions
import client.ClientResponse
import client.LeaderboardPlayer
import client.SourcePlaylist
import kotlinx.coroutines.flow.transform
import toClientRoom
import totalPoints

data class UserRoomMachine(val roomMachine: RoomMachine, val user: User) {
    suspend fun handleAction(action: UserAction) = roomMachine.handleAction(action, user)
    val responses = roomMachine.actionResultFlow.transform { result ->
        println("transforming actionResults, result = $result")
        when(result) {
            is ActionResult.Applied -> {
                val (room, action, actionUser) = result
                when(action) {
                    is GameAction -> {
                        val game = room.state as RoomState.Game.Server
                        when(val action = action) {
                            GameAction.Answer.NextScreen -> when(val newGameState = game.gameScreen) {
                                is GameScreen.Question -> {
                                    val question = game.questions[newGameState.questionIndex]
                                    emit(ClientResponse.Room.Game.SendQuestion(newGameState.questionIndex, question.lyric, if (game.config.showSourcePlaylist) SourcePlaylist.Shown(question.playlist) else SourcePlaylist.NotShown))
                                }
                                is GameScreen.Summary -> emit(ClientResponse.Room.Game.Ended)
                            }
                            is GameAction.Question.AnswerQuestion -> {
                                when (actionUser) {
                                    user -> emit(ClientResponse.Room.Game.SendAnswer(action.questionIndex, game.questions[action.questionIndex].toClientAnswered(game.userStates.getValue(actionUser).answers[action.questionIndex] as GameAnswer.Answered)))
                                    else -> {
                                        val userState = game.userStates.getValue(actionUser)
                                        emit(ClientResponse.Room.Game.UpdateLeaderboard(LeaderboardPlayer(actionUser, userState.answers.totalPoints(), userState.answers.answeredQuestions(), userState.connected)))
                                    }
                                }
                            }
                            is GameAction.Question.RequestHint -> when{
                                actionUser != user -> return@transform
                                action.hint is Hint.Artist -> emit(ClientResponse.Room.Game.SendHint.Artist(action.questionIndex, game.questions[action.questionIndex].artist))
                                action.hint is Hint.NextLyric -> emit(ClientResponse.Room.Game.SendHint.NextLyric(action.questionIndex, game.questions[action.questionIndex].nextLyric))
                            }
                        }
                    }
                    is LobbyAction -> {
                        val lobby = room.state as RoomState.Lobby
                        when(val action = action) {
                            is LobbyAction.Host.UpdatePlaylists -> emit(ClientResponse.Room.Lobby.PlaylistsUpdated(lobby.playlists))
                            is LobbyAction.Host.KickUser -> when (action.kicking) {
                                user -> emit(ClientResponse.Kicked)
                                else -> emit(ClientResponse.Room.UsersUpdated(lobby.joinedUsers, room.host))
                            }
                            is LobbyAction.Host.UpdateConfig -> emit(ClientResponse.Room.Lobby.ConfigUpdated(action.updatedConfig))
                        }
                    }
                    UserAction.Host.LoadGame -> emit(ClientResponse.Room.Loading)
                    UserAction.JoinRoom -> when (actionUser) {
                        user -> emit(ClientResponse.Joined(room.toClientRoom(user)))
                        else -> {
                            val joinedUsers = when(room.state) {
                                is RoomState.Lobby -> room.state.joinedUsers
                                is RoomState.Loading -> room.state.users
                                is RoomState.Game.Server -> room.state.userStates.filter { it.value.connected }.keys.toList()
                            }
                            emit(ClientResponse.Room.UsersUpdated(joinedUsers, room.host))
                        }
                    }
                    UserAction.LeaveRoom -> when (actionUser) {
                        user -> emit(ClientResponse.Left)
                        else -> {
                            val joinedUsers = when(room.state) {
                                is RoomState.Lobby -> room.state.joinedUsers
                                is RoomState.Loading -> room.state.users
                                is RoomState.Game.Server -> room.state.userStates.filter { it.value.connected }.keys.toList()
                            }
                            emit(ClientResponse.Room.UsersUpdated(joinedUsers, room.host))
                        }
                    }
                    is ServerAction.StartGame -> {
                        val game = room.state as RoomState.Game.Server
                        val gameState = game.gameScreen as GameScreen.Question
                        val question = game.questions[gameState.questionIndex]
                        emit(ClientResponse.Room.Game.SendQuestion(gameState.questionIndex, question.lyric, if (game.config.showSourcePlaylist) SourcePlaylist.Shown(question.playlist) else SourcePlaylist.NotShown))
                    }
                }

            }
            is ActionResult.Error -> {
                val (error, actionUser) = result
                when(error) {
                    ServerError.WrongQuestion,
                    ServerError.AlreadyAnswered,
                    ServerError.NotInLobby,
                    ServerError.NotInGame,
                    ServerError.NotAHost,
                    ServerError.UserAlreadyLeft,
                    ServerError.KickedYourself,
                    ServerError.InvalidConfig,
                    ServerError.AlreadyInGame,
                    ServerError.WrongScreen,
                    ServerError.AlreadyReceivedHint -> if (user == actionUser) emit(ClientResponse.Error(error))
                }
            }
        }
    }
}