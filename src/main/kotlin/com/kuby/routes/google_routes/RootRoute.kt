package com.kuby.routes.google_routes

import com.kuby.domain.model.EndPoint
import com.kuby.domain.model.GoogleEndPoint
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.rootRoute(){
    get(GoogleEndPoint.Root.path) {
        call.respondText("Welcome to Ktor Server!")
    }
}