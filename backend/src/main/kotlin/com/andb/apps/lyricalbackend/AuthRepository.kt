package com.andb.apps.lyricalbackend

import User

interface AnonymousAuthRepository {
    fun addUser(user: User.Anonymous): Pair<String, String>
    fun
}