package com.example.impressmap.database.firebase.entites

interface Post {
    val id: String
    val markerId: String
    val creatorId: String
    val text: String
    val dateTime: Int
}