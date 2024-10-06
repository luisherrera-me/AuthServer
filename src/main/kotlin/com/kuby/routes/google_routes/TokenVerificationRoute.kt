package com.kuby.routes.google_routes

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.kuby.domain.model.*
import com.kuby.domain.repository.UserDataSource
import com.kuby.util.Constants.AUDIENCE
import com.kuby.util.Constants.ISSUER
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

fun Route.tokenVerificationRoute(
app: Application,
userDataSource: UserDataSource
) {
    post(GoogleEndPoint.TokenVerification.path) {
        val request = call.receive<ApiRequest>()
        if (request.tokenId.isNotEmpty()) {
            val result = verifyGoogleTokenId(tokenId = request.tokenId)
            if (result != null) {
                saveUserToDatabase(
                    app = app,
                    result = result,
                    userDataSource = userDataSource
                )
            } else {
                app.log.info("TOKEN VERIFICATION FAILED")
                call.respondRedirect(EndPoint.Unauthorized.path)
            }
        } else {
            app.log.info("EMPTY TOKEN ID")
            call.respondRedirect(EndPoint.Unauthorized.path)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.saveUserToDatabase(
    app: Application,
    result: GoogleIdToken,
    userDataSource: UserDataSource
) {
    val sub = result.payload["sub"].toString()
    val name = result.payload["name"].toString()
    val emailAddress = result.payload["email"].toString()
    val profilePhoto = result.payload["picture"].toString()
    val user = User(
        id = sub,
        name = name,
        emailAddress = emailAddress,
        profilePhoto = profilePhoto
    )

    val response = userDataSource.saveUserInfo(user = user)
    if (response) {
        app.log.info("USER SUCCESSFULLY SAVED/RETRIEVED")
        call.sessions.set(UserSession(id = sub, name = name))
        call.respondRedirect(GoogleEndPoint.Authorized.path)
    } else {
        app.log.info("ERROR SAVING THE USER")
        call.respondRedirect(EndPoint.Unauthorized.path)
    }
}


fun verifyGoogleTokenId(tokenId: String): GoogleIdToken? {
    return try {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(AUDIENCE))
            .setIssuer(ISSUER)
            .build()
        verifier.verify(tokenId)
    } catch (e: Exception) {
        null
    }
}