package com.kuby.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ApiResponse(
    val user: User? = null,
    val token: String? = null
)

@Serializable
data class ApiResponseError(
    val statusCode: Int = 500,
    val message: String = ""
)

