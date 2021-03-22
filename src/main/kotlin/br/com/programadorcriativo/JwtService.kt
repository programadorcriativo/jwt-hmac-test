package br.com.programadorcriativo

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.HmacAlgorithms
import org.apache.commons.codec.digest.HmacUtils
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

open class JwtService {

    private val jwtLifeTimeMinutes: Int = 5
    private val mapper = jacksonObjectMapper()

    fun tokenGenerate(key: String, header: JwtHeader, payload: JwtPayload): String {
        val jsonHeader = mapper.writeValueAsString(header)
        val jsonPayload = mapper.writeValueAsString(payload)
        val content = "${toBase64(jsonHeader.toByteArray())}.${toBase64(jsonPayload.toByteArray())}"
        val sign = signWithBase64(key, content)
        return "${content}.$sign"

    }

    fun tokenIsValid(key: String, token: String) {
        val tokenParts = token.split(".")
        val header = tokenParts[0]
        val payload = tokenParts[1]
        val sign = tokenParts[2]

        signValidate(key, header, payload, sign)
        isExpired(payload)
    }

    private fun signValidate(key:String, header: String, payload: String, sign: String) {
        val checkedSign = signWithBase64(key, "${header}.$payload")

        if (!sign.equals(checkedSign)) {
            throw TokenSignatureInvalidException("Invalid jwt signature")
        }
    }

    private fun isExpired(payload: String) {
        val payloadDecoded = decode(payload)
        val jwtPayload = mapper.readValue(payloadDecoded, JwtPayload::class.java)

        val expirationTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(jwtPayload.exp), ZoneOffset.UTC);
        val now = LocalDateTime.now()

        val duration = Duration.between(expirationTime, now).abs().toMinutes()

        if (duration > jwtLifeTimeMinutes) {
            throw TokenExpiredException("Token expired")
        }
    }

    private fun toBase64(content: ByteArray) = Base64.encodeBase64URLSafeString(content)

    private fun decode(content: String) = Base64.decodeBase64(content)

    private fun signWithBase64(key: String, content: String) = toBase64(sign(key, content))

    private fun sign(key: String, content: String) = HmacUtils(HmacAlgorithms.HMAC_SHA_256, key.toByteArray()).hmac(content)
}