package com.kuby.di

import com.kuby.data.repository.UserDataSourceImpl
import com.kuby.domain.repository.UserDataSource
import com.kuby.util.Constants.DATABASE_NAME
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val KoinModule = module {
    single {
        KMongo.createClient(System.getenv("MONGODB_URI"))
        //KMongo.createClient(System.getenv("MONGODB_URI"))
            .coroutine
            .getDatabase(DATABASE_NAME)
    }

    single<UserDataSource> {
        UserDataSourceImpl(get())
    }
}