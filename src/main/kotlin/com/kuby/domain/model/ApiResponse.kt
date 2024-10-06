package com.kuby.domain.model

import kotlinx.serialization.Serializable

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


//Google API Response
@Serializable
data class GoogleApiResponse (
    val googleSuccess: Boolean,
    val googleUser: User? = null,
    val googleMessage: String? = null
)

