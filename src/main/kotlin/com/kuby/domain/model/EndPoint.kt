package com.kuby.domain.model

sealed class EndPoint (val path: String) {

    data object Auth: EndPoint(path = "/api/v1/auth")
    data object DataUser: EndPoint(path = "/api/v1/user")
    data object SignUp: EndPoint(path = "/sign_up")
    data object SignIn: EndPoint(path = "/sign_in")
    data object Unauthorized: EndPoint(path = "/unauthorized")
}

//Google Auth Endpoint
sealed class GoogleEndPoint (val path: String) {
    object Root: GoogleEndPoint(path = "/")
    object TokenVerification: GoogleEndPoint(path = "/token_verification")
    object GetUserInfo: GoogleEndPoint(path = "/get_user")
    object UptadeUserInfo: GoogleEndPoint(path = "/update_user")
    object DeleteUser: GoogleEndPoint(path = "/delete_user")
    object SignOut: GoogleEndPoint(path = "/sign_out")
    object Unauthorized: GoogleEndPoint(path = "/unauthorized")
    object Authorized: GoogleEndPoint(path = "/authorized")
}
