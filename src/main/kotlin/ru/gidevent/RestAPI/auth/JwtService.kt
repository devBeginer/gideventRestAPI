package ru.gidevent.RestAPI.auth

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class JwtService(
        @Value("\${jwt.secret.access}") val jwtAccessSecret: String,
        @Value("\${jwt.secret.refresh}") val jwtRefreshSecret: String
) {
    /*@Value("\${security.jwt.secret-key}")
    private val secretKey: String? = null

    @Value("\${security.jwt.expiration-time}")
    val expirationTime: Long = 0*/
    private val jwtAccessKey: Key
    private val jwtRefreshKey: Key


    init{
        jwtAccessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        jwtRefreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    fun generateAccessToken(user: User): String {
        val now = LocalDateTime.now()
        val accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant()
        val accessExpiration = Date.from(accessExpirationInstant)
        return Jwts.builder()
                .setSubject(user.login)
                .setExpiration(accessExpiration)
                .signWith(jwtAccessKey)
                .claim("roles", user.roles)
                .claim("firstName", user.firstName)
                .compact()
    }

    fun generateRefreshToken(user: User): String {
        val now = LocalDateTime.now()
        val refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant()
        val refreshExpiration = Date.from(refreshExpirationInstant)
        return Jwts.builder()
                .setSubject(user.login)
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshKey)
                .compact()
    }

    fun validateAccessToken(accessToken: String): Boolean {
        return validateToken(accessToken, jwtAccessKey)
    }

    fun validateRefreshToken(refreshToken: String): Boolean {
        return validateToken(refreshToken, jwtRefreshKey)
    }

    private fun validateToken(token: String, secret: Key): Boolean {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
            return true
        } catch (expEx: ExpiredJwtException) {
            //log.error("Token expired", expEx)
            println("Token expired $expEx")
        } catch (unsEx: UnsupportedJwtException) {
            //log.error("Unsupported jwt", unsEx)
            println("Unsupported jwt $unsEx")
        } catch (mjEx: MalformedJwtException) {
            //log.error("Malformed jwt", mjEx)
            println("Malformed jwt $mjEx")
        } catch (sEx: SignatureException) {
            //log.error("Invalid signature", sEx)
            println("Invalid signature $sEx")
        } catch (e: Exception) {
            //log.error("invalid token", e)
            println("invalid token $e")
        }
        return false
    }

    fun getAccessClaims(token: String): Claims {
        return getClaims(token, jwtAccessKey)
    }
    fun getRefreshClaims(token: String): Claims {
        return getClaims(token, jwtRefreshKey)
    }

    private fun getClaims(token: String, secret: Key): Claims {
        return Jwts
                .parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .body
    }

    /*fun extractUsername(token: String?): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(HashMap(), userDetails)
    }

    fun generateToken(extraClaims: Map<String?, Any?>, userDetails: UserDetails): String {
        return buildToken(extraClaims, userDetails, expirationTime)
    }

    private fun buildToken(
            extraClaims: Map<String?, Any?>,
            userDetails: UserDetails,
            expiration: Long
    ): String {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.username)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + expiration))
                .signWith(signInKey, SignatureAlgorithm.HS256)
                .compact()
    }

    fun isTokenValid(token: String?, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String?): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts
                .parserBuilder()
                .setSigningKey(signInKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
    }

    private val signInKey: Key
        private get() {
            val keyBytes = Decoders.BASE64.decode(secretKey)
            return Keys.hmacShaKeyFor(keyBytes)
        }*/
}