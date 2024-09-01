package com.example.impressmap.database.firebase.entites

interface Comment {
    val id: String
    val postId: String
    val creatorId: String
    val text: String
    val dateTime: Int
}