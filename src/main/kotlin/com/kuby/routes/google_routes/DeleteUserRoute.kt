package com.kuby.routes.google_routes

import com.kuby.domain.model.EndPoint
import com.kuby.domain.model.GoogleApiResponse
import com.kuby.domain.model.GoogleEndPoint
import com.kuby.domain.model.UserSession
import com.kuby.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

fun Route.deleteGoogleUserRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    authenticate("auth-session") {
        delete(GoogleEndPoint.DeleteUser.path){
            val userSession = call.principal<UserSession>()
            if (userSession == null){
                app.log.info("INVALID SESSION")
                call.respondRedirect(EndPoint.Unauthorized.path)
            } else {
                try {
                    call.sessions.clear<UserSession>()
                    deleteUserFromDb(
                        app = app,
                        userId = userSession.id,
                        userDataSource = userDataSource
                    )
                } catch (e: Exception){
                    app.log.info("DELETING USER INFO ERROR: ${e.message}")
                    call.respondRedirect(EndPoint.Unauthorized.path)
                }
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.deleteUserFromDb(
    app: Application,
    userId: String,
    userDataSource: UserDataSource
){
    val result = userDataSource.deleteUser(userId = userId)
    if (result){
        app.log.info("USER SUCCESSFULLY DELETED")
        call.respond(
            message = GoogleApiResponse(googleSuccess = true),
            status = HttpStatusCode.OK
        )
    } else {
        app.log.info("ERROR DELETING THE USER")
        call.respond(
            message = GoogleApiResponse(googleSuccess = false),
            status =  HttpStatusCode.BadRequest
        )
    }
}