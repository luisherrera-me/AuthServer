package com.kuby.routes

import com.kuby.domain.model.*
import com.kuby.domain.repository.UserDataSource
import com.kuby.service.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signInRoute(
    jwtService: JwtService,
    userDataSource: UserDataSource
){
    post (EndPoint.SignIn.path){
        val loginRequest = call.receive<LoginRequest>()
        val foundUser = userDataSource.getUserInfoByEmail(loginRequest.emailAddress)
        if (foundUser != null){
            val token: String? = jwtService.createJwtToken(loginRequest)
            token?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = User(
                        id = foundUser.id,  // Include user ID
                        name = foundUser.name,  // Include user name
                        lastName = foundUser.lastName,  // Include last name if needed
                        phone = foundUser.phone,  // Include phone if needed
                        createdAt = foundUser.createdAt,  // Include creation date
                        updatedAt = foundUser.updatedAt,  // Include update date
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
        }else{
            call.respond(
                status = HttpStatusCode.Forbidden,
                message = ApiResponseError(
                    statusCode = 403,
                    message = "Email Error"
                )
            )
        }

    }
}
