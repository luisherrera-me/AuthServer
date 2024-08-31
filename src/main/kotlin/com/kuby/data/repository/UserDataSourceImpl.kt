package com.kuby.data.repository

import com.kuby.domain.model.User
import com.kuby.domain.model.UserUpdate
import com.kuby.domain.repository.UserDataSource
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import java.time.LocalDateTime

class UserDataSourceImpl(
    database: CoroutineDatabase
): UserDataSource {

    private  val users = database.getCollection<User>()

    override suspend fun getUserInfoById(id: String): User? {
        return users.findOne(filter = User::id eq id)
    }

    override suspend fun getUserInfoByEmail(emailAddress: String): User? {
        val regex = "^${Regex.escape(emailAddress)}$".toRegex(setOf(RegexOption.IGNORE_CASE))
        return users.findOne(User::emailAddress.regex(regex))
    }

    override suspend fun saveUserInfo(user: User): Boolean {
        val existingUser = users.findOne(filter = User::emailAddress eq user.emailAddress)
        println("Existing user: $existingUser")
        return if (existingUser == null){
            val result = users.insertOne(document = user)
            result.wasAcknowledged()
        } else{
            false
        }
    }

    override suspend fun deleteUser(userId: String): Boolean {
        return users.deleteOne(filter = User::id eq userId).wasAcknowledged()
    }

    override suspend fun updateUserInfo(
        id: String,
        name: String,
        updatedAt: LocalDateTime,
        profilePhoto: String
    ): Boolean {
        val updates = combine(
            setValue(UserUpdate::name, name),
            setValue(UserUpdate::updatedAt, updatedAt.toString()),
            setValue(UserUpdate::profilePhoto, profilePhoto)
        )
        return users.updateOne(
            filter = User::id eq id,
            update = updates
        ).wasAcknowledged()
    }
}