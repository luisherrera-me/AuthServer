package com.kuby.routes

import com.kuby.domain.model.ApiResponse
import com.kuby.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.lang.Exception

fun Route.userRoute(app: Application, userDataSource: UserDataSource) {

    authenticate("another-auth") {
        get("/{id}") {
            val id: String = call.parameters["id"]
                ?: return@get call.respond(HttpStatusCode.BadRequest)

            try {
                val foundUser = userDataSource.getUserInfoById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)

                val principalID = extractPrincipalUsername(call)
                if (foundUser.id != principalID) {
                    call.respond(HttpStatusCode.Unauthorized)
                } else {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = foundUser
                    )
                }
            } catch (e: Exception) {
                application.log.error("Error al obtener información del usuario: ${e.message}")
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}


private fun extractPrincipalUsername(call: ApplicationCall): String? =
    call.principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("UserId")
        ?.asString()
