package com.funnyjack.monolith.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date

object JwtUtil {
    private const val SECRET_KEY = "funnyJack" // 在生产环境中应该使用更安全的方式存储
    private const val EXPIRATION_TIME: Long = 24 * 60 * 60 * 1000 // 24小时 (毫秒)


    fun generateToken(openid: String): String {
        val now = Date()
        val expiryDate = Date(now.time + EXPIRATION_TIME)

        return Jwts.builder()
            .setSubject(openid)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun getOpenidFromToken(token: String): String? {
        return try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body.subject
        } catch (e: Exception) {
            null
        }
    }
}