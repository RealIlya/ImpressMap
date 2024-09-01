package com.example.impressmap.database.firebase.entites

interface User {
    val id: String  // it may be generic
    val avatarId: String
    val name: String
    val surname: String
    val email: String
    val phoneNumber: String
}