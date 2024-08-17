package com.kuby.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    val emailAddress: String,
    val password: String,
)