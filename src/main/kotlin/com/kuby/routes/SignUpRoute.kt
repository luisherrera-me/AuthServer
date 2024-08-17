package com.kuby.routes

import com.kuby.domain.model.*
import com.kuby.domain.repository.UserDataSource
import com.kuby.service.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import java.time.LocalDateTime
import java.util.*

fun Route.signUpRoute (
    jwtService: JwtService,
    userDataSource: UserDataSource
){
    post (EndPoint.SignUp.path){
        try {
            val userRequest = call.receive<User>()

            saveUserToDatabase(
                userRequest,
                jwtService,
                userDataSource
            )

        } catch (e: Exception) {
            call.respond(
                status = HttpStatusCode.Forbidden,
                message = ApiResponseError(
                    statusCode = 403,
                    message = "Error al registrar usuario"
                )
            )
        }
    }
}

private fun User.toModel(): User =

    User(
        id = UUID.randomUUID().toString(),
        emailAddress = this.emailAddress,
        name = this.name,
        lastName = this.lastName,
        phone = this.phone,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        password = this.password
    )

private suspend fun PipelineContext<Unit, ApplicationCall>.saveUserToDatabase(
    userRequest: User,
    jwtService: JwtService,
    userDataSource: UserDataSource
) {
    val user = userRequest.toModel()
    val response = userDataSource.saveUserInfo(user)
    val foundUser = user.emailAddress?.let { userDataSource.getUserInfoByEmail(it) }
    val tokenId = user.emailAddress?.let {
        user.password?.let { it1 ->
            LoginRequest(
            emailAddress = it,
            password = it1
        )
        }
    }
    return if (response && tokenId != null) {
        val token: String? = jwtService.createJwtToken(tokenId)
        token?.let {
            call.respond(
                status = HttpStatusCode.OK,
                message = ApiResponse(
                    user = foundUser,
                    token = token

                )
            )
        } ?: call.respond(
            status = HttpStatusCode.Forbidden,
            message = ApiResponseError(
                statusCode = 403,
                message = "password Error"
            )
        )
    } else {
        // Si el usuario ya existe
        call.respond(
            status = HttpStatusCode.Forbidden,
            message = ApiResponseError(
                statusCode = 403,
                message = "Email ya esta en uso."
            )
        )
    }
}