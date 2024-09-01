package com.example.impressmap.database.firebase.entites

interface SubComment {
    val id: String
    val commentId: String
    val creatorId: String
    val text: String
    val dateTime: Int
}