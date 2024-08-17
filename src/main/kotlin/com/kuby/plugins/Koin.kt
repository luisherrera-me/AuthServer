package com.kuby.plugins

import com.kuby.di.KoinModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(){
    install(Koin){
        modules(KoinModule)
    }
}