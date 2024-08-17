package com.kuby.plugins

import com.kuby.domain.repository.UserDataSource
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent

fun Application.configureRouting() {
    routing {
        val userDataSource: UserDataSource by KoinJavaComponent.inject(UserDataSource::class.java)
    }
}
