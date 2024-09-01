package com.example.impressmap.database

import com.example.impressmap.database.firebase.entites.User

interface UsersRepo {
    suspend fun select(id: String): User
    suspend fun insert(user: User)
    suspend fun update(user: User)
    suspend fun delete(user: User)
}
