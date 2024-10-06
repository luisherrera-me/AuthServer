package com.kuby.routes.google_routes

import com.kuby.domain.model.GoogleEndPoint
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unauthorizedRoute() {
    get(GoogleEndPoint.Unauthorized.path) {
        call.respond(
            message = "NOT AUTHORIZED",
            status = HttpStatusCode.Unauthorized
        )
    }
}