package com.kuby.routes

import com.kuby.domain.model.ApiResponse
import com.kuby.domain.model.EndPoint
import com.kuby.domain.model.GoogleApiResponse
import com.kuby.domain.model.GoogleEndPoint
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authorizedRoute() {
    authenticate("auth-session"){
        get(GoogleEndPoint.Authorized.path) {
            call.respond(
                message = GoogleApiResponse(googleSuccess = true),
                status = HttpStatusCode.OK
            )
        }
    }
}