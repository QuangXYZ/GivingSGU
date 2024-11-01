package com.sgu.givingsgu.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT

object TokenUtils {

    fun decodeToken(token: String): DecodedJWT {
        return JWT.decode(token)
    }

    fun getClaimFromToken(token: String, claim: String): String? {
        val decodedJWT = decodeToken(token)
        return when (claim) {
            "sub" -> decodedJWT.getClaim(claim).asString()
            "exp" -> decodedJWT.getClaim(claim).asLong()?.toString()
            "iat" -> decodedJWT.getClaim(claim).asLong()?.toString()
            else -> decodedJWT.getClaim(claim).asString()
        }

    }

//    fun getAllClaimsFromToken(token: String): Map<String, Any?> {
//        val decodedJWT = decodeToken(token)
//        val claims = decodedJWT.claims
//        val claimsMap = mutableMapOf<String, Any?>()
//        for ((key, value) in claims) {
//            claimsMap[key] = value.asString() ?: value.asBoolean() ?: value.asInt() ?: value.asLong() ?: value.asDouble() ?: value.asDate() ?: value.asArray(String::class.java) ?: value.asMap()
//        }
//        return claimsMap
//    }

}