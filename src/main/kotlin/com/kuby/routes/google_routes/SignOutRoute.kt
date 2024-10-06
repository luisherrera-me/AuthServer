package com.kuby.routes.google_routes

import com.kuby.domain.model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.signOutRoute(){
    authenticate("auth-session"){
        get(GoogleEndPoint.SignOut.path){
            call.sessions.clear<UserSession>()
            call.respond(
                message = GoogleApiResponse(googleSuccess = true),
                status = HttpStatusCode.OK
            )
        }
    }
}