package com.kuby.domain.repository

import com.kuby.domain.model.User
import java.time.LocalDateTime

interface UserDataSource {
    suspend fun  getUserInfoById(id: String): User?
    suspend fun  getUserInfoByEmail(emailAddress: String): User?
    suspend fun saveUserInfo(user: User): Boolean
    suspend fun deleteUser(userId: String): Boolean
    suspend fun  updateUserInfo(
        id: String,
        name: String,
        updatedAt: LocalDateTime,
        profilePhoto: String
    ): Boolean
}


interface GoogleUserDataSource {
    suspend fun  getUserInfo(userId: String): User?
    suspend fun saveUserInfo(user: User): Boolean
    suspend fun deleteUser(userId: String): Boolean
    suspend fun  updateUserInfo(
        userId: String,
        firstName: String,
        lastName: String
    ): Boolean
}