package br.com.programadorcriativo

data class JwtHeader(val alg: String = "sha256", val typ: String = "JWT")

data class User(val name: String, val email: String)

data class JwtPayload(val iat: Long, val exp: Long, val user: User)

data class JwtToken(val token: String)