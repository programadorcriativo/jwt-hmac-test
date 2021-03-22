package br.com.programadorcriativo

import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class HmacAppTest {

    val jwtService = JwtService()

    @Test
    fun generateTokenTest() {
        val now = LocalDateTime.now()

        val header = JwtHeader(alg = "HS256")

        val payload = JwtPayload(
            iat = now.toEpochSecond(ZoneOffset.UTC),
            exp = now.plusMinutes(5).toEpochSecond(ZoneOffset.UTC),
            user = User(name = "Programador Criativo", email = "programadorcriativooficial@gmail.com")
        )

        val token = jwtService.tokenGenerate("123456", header, payload)

        Assert.assertNotNull(token)
        Assert.assertEquals(token.split(".").size, 3)
    }

    @Test(expected = TokenExpiredException::class)
    fun `Given a expired token then receive a TokenExpiredException`() {
        val now = LocalDateTime.now()
        val header = JwtHeader(alg = "HS256")
        val payload = JwtPayload(
            iat = now.toEpochSecond(ZoneOffset.UTC),
            exp = now.minusMinutes(6).toEpochSecond(ZoneOffset.UTC),
            user = User(name = "Programador Criativo", email = "programadorcriativooficial@gmail.com")
        )

        val key = "654321"
        val token = jwtService.tokenGenerate(key, header, payload)
        jwtService.tokenIsValid(key, token)
    }

    @Test(expected = TokenSignatureInvalidException::class)
    fun `Given a payload with invalid signature then receive a TokenSignatureInvalidException`() {
        val now = LocalDateTime.now()
        val header = JwtHeader(alg = "HS256")

        val payload = JwtPayload(
            iat = now.toEpochSecond(ZoneOffset.UTC),
            exp = now.minusMinutes(6).toEpochSecond(ZoneOffset.UTC),
            user = User(name = "Programador Criativo", email = "programadorcriativooficial@gmail.com")
        )

        val key = "654321"
        val token = jwtService.tokenGenerate(key, header, payload)
        val tokenParts = token.split(".")
        val tokenWithInvalidSignature = "${tokenParts[0]}.${tokenParts[1]}.wQR1ags0tipJfqMtNp6SiITIMoONblVKq7N5K_L8WK0"

        jwtService.tokenIsValid(key, tokenWithInvalidSignature)
    }

}
