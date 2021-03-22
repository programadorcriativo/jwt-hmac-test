package br.com.programadorcriativo

import java.lang.RuntimeException

data class TokenExpiredException(override val message: String): RuntimeException(message)

data class TokenSignatureInvalidException(override val message: String): RuntimeException(message)