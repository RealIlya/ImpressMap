package com.example.impressmap.database.firebase.repos

import com.example.impressmap.database.UsersRepo
import com.example.impressmap.database.firebase.entites.User
import com.example.impressmap.util.Constants.DATABASE_REF
import com.example.impressmap.util.Constants.Keys
import com.example.impressmap.util.Constants.Keys.USERS_NODE
import kotlinx.coroutines.tasks.await
import java.util.HashMap

class UsersRepoImpl : UsersRepo {
    private val usersRef = DATABASE_REF.child(USERS_NODE)

    override suspend fun select(id: String): User {
        return usersRef.child(id).get().await().getValue(UserImpl::class.java) ?: UserImpl()
    }

    override suspend fun insert(user: User) {
        val data = toMap(user)
        usersRef.child(user.id).updateChildren(data).await()
    }

    override suspend fun update(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(user: User) {
        TODO("Not yet implemented")
    }

    private fun toMap(user: User): Map<String, Any> {
        val data = HashMap<String, Any>()

        data[Keys.CHILD_ID_NODE] = user.id
        data[Keys.NAME_NODE] = user.name
        data[Keys.SURNAME_NODE] = user.surname
        data[Keys.EMAIL_NODE] = user.email
        data[Keys.PHONE_NUMBER_NODE] = user.phoneNumber
        data[Keys.AVATAR_ID_NODE] = user.avatarId

        return data
    }

    class UserImpl(
        override val id: String = "",
        override val avatarId: String = "",
        override val name: String = "",
        override val surname: String = "",
        override val email: String = "",
        override val phoneNumber: String = ""
    ) : User
}
