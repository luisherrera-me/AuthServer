package com.kuby.routes

import com.kuby.domain.model.EndPoint
import com.kuby.domain.repository.UserDataSource
import com.kuby.service.JwtService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent

fun Application.configureRouting(
    jwtService: JwtService,
) {

}