package com.kuby.routes

import com.kuby.domain.model.*
import com.kuby.domain.repository.GoogleUserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Route.updateUserInfoRoute(
    app: Application,
    userDataSource: GoogleUserDataSource
) {
    authenticate("auth-session") {
        put(GoogleEndPoint.UptadeUserInfo.path){
            val userSession = call.principal<UserSession>()
            val userUpdate = call.receive<GoogleUserUpdate>()
            if (userSession == null){
                app.log.info("INVALID SESSION")
                call.respondRedirect(GoogleEndPoint.Unauthorized.path)
            } else {
                try {
                    updateGoogleUserInfo(
                        app = app,
                        userId = userSession.id,
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

private suspend fun PipelineContext<Unit, ApplicationCall>.updateGoogleUserInfo(
    app: Application,
    userId: String,
    userUpdate: GoogleUserUpdate,
    userDataSource: GoogleUserDataSource
){
    val response = userDataSource.updateUserInfo(
        userId = userId,
        firstName = userUpdate.firstName,
        lastName = userUpdate.lastName,

    )
    if (response){
        app.log.info("USER SUCCESSFULLY UPDATE")
        call.respond(
            message = GoogleApiResponse(
                googleSuccess = true,
                googleMessage = "Successfully Updated!"
            ),
            status = HttpStatusCode.OK
        )
    } else {
        app.log.info("ERROR UPDATING THE USER")
        call.respond(
            message = GoogleApiResponse(googleSuccess = false),
            status = HttpStatusCode.BadRequest
        )
    }
}