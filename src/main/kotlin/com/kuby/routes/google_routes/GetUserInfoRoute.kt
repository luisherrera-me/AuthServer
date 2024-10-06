package com.kuby.routes.google_routes

import com.kuby.domain.model.*
import com.kuby.domain.repository.GoogleUserDataSource
import com.kuby.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.lang.Exception

fun Route.getUserInfoRoute(
    app: Application,
    userDataSource: GoogleUserDataSource
) {
    authenticate("auth-session"){
        get(GoogleEndPoint.GetUserInfo.path) {
            val userSession = call.principal<UserSession>()

            if (userSession == null){
                app.log.info("INVALID SESSION")
                call.respondRedirect(EndPoint.Unauthorized.path)
            } else {
                try {
                    call.respond(
                        message = GoogleApiResponse(
                            googleSuccess = true,
                            googleUser = userDataSource.getUserInfo(userId = userSession.id)
                        ),
                        status = HttpStatusCode.OK
                    )
                } catch (e: Exception){
                    app.log.info("GETTING USER INFO ERROR: ${e.message}")
                    call.respondRedirect(EndPoint.Unauthorized.path)
                }
            }
        }
    }
}