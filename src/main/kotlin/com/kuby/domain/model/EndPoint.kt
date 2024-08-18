package com.kuby.domain.model

sealed class EndPoint (val path: String) {

    data object Auth: EndPoint(path = "/api/v1/auth")
    data object DataUser: EndPoint(path = "/api/v1/user")
    data object SignUp: EndPoint(path = "/sign_up")
    data object SignIn: EndPoint(path = "/sign_in")
    data object Unauthorized: EndPoint(path = "/unauthorized")
}