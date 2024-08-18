package com.kuby.routes

import com.kuby.domain.repository.UserDataSource
import com.kuby.service.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteUserRoute (
    app: Application,
    userDataSource: UserDataSource
){
    authenticate("another-auth"){
        delete("/{id}"){
            val id: String = call.parameters["id"]
                ?: return@delete call.respond(
                    status = HttpStatusCode.NotFound,
                    message = "credenciales nulas"
                )

            try {
                val principalID = extractPrincipalUsername(call)
                if (id != principalID) {
                    call.respond(HttpStatusCode.Unauthorized)
                } else {
                    userDataSource.deleteUser(id)
                    call.respond(HttpStatusCode.OK)
                }
            }catch (_: Exception){
                call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = "credenciales no validad"
                )
            }
        }
    }
}

private fun extractPrincipalUsername(call: ApplicationCall): String? =
    call.principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("UserId")
        ?.asString()
