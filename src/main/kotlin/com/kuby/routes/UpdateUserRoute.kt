package com.kuby.routes

import com.kuby.domain.model.ApiResponse
import com.kuby.domain.model.EndPoint
import com.kuby.domain.model.User
import com.kuby.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import java.lang.Exception
import java.time.LocalDateTime

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
            app.log.info("USER SUCCESSFULLY UPDATE")
            call.respond(
                message = ApiResponse(

                ),
                status = HttpStatusCode.OK
            )
        } else {
            app.log.info("ERROR UPDATING THE USER")
            call.respond(
                message = ApiResponse(),
                status = HttpStatusCode.BadRequest
            )
        }

    } else {
        app.log.info("ERROR UPDATING THE USER")
        call.respond(
            message = ApiResponse(),
            status = HttpStatusCode.BadRequest
        )
    }

}


private fun extractPrincipalUsername(call: ApplicationCall): String? =
    call.principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("UserId")
        ?.asString()
