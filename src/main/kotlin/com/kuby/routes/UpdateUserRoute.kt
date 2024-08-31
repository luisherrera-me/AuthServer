package com.kuby.routes

import com.kuby.domain.model.ApiResponse
import com.kuby.domain.model.ApiResponseError
import com.kuby.domain.model.EndPoint
import com.kuby.domain.model.User
import com.kuby.domain.repository.UserDataSource
import com.kuby.util.LocalDateTimeSerializer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable
import java.lang.Exception
import java.time.LocalDateTime
import java.util.*

fun Route.updateUserRoute(app: Application, userDataSource: UserDataSource) {

    authenticate("another-auth") {
        put(){
            val userSession = extractPrincipalUsername(call)
            val userUpdate = call.receive<User>()
            if (userSession == null){
                app.log.info("INVALID SESSION")
                call.respondRedirect(EndPoint.Unauthorized.path)
            } else {
                try {
                    updateUserInfo(
                        app = app,
                        userId = userSession,
                        userUpdate = userUpdate,
                        userDataSource = userDataSource
                    )
                } catch (e: Exception){
                    app.log.info("UPDATE USER INFO ERROR: ${e.message}")
                    call.respondRedirect(EndPoint.Unauthorized.path)
                }
            }
        }
    }
}



private suspend fun PipelineContext<Unit, ApplicationCall>.updateUserInfo(
    app: Application,
    userId: String,
    userUpdate: User,
    userDataSource: UserDataSource
){
    if (userUpdate.name != null && userUpdate.profilePhoto != null) {
        val response = userDataSource.updateUserInfo(
            id = userId,
            name = userUpdate.name,
            updatedAt = LocalDateTime.now(),
            profilePhoto = userUpdate.profilePhoto
        )

        if (response){
            call.respond(
                message = ApiResponseError(
                    statusCode = 200,
                    message = "USER SUCCESSFULLY UPDATE"
                ),
                status = HttpStatusCode.OK
            )
        } else {
            call.respond(
                message = ApiResponseError(
                    statusCode = 400,
                    message = "ERROR UPDATING THE USER"
                ),
                status = HttpStatusCode.BadRequest
            )
        }

    } else {
        call.respond(
            message = ApiResponseError(
                statusCode = 400,
                message = "ERROR UPDATING THE USER"
            ),
            status = HttpStatusCode.BadRequest
        )
    }

}


private fun extractPrincipalUsername(call: ApplicationCall): String? =
    call.principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("UserId")
        ?.asString()
