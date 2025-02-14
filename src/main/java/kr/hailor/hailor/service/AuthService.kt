package kr.hailor.hailor.service

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import kr.hailor.hailor.config.properties.OAuthProperties
import kr.hailor.hailor.controller.auth.LoginRequest
import kr.hailor.hailor.controller.auth.ServiceTokensResponse
import kr.hailor.hailor.controller.auth.SignUpRequest
import kr.hailor.hailor.enity.AgreedTerms
import kr.hailor.hailor.enity.Role
import kr.hailor.hailor.enity.User
import kr.hailor.hailor.exception.AlreadyRegisteredUserException
import kr.hailor.hailor.exception.NotAgreedAllRequiredTermsException
import kr.hailor.hailor.exception.NotRegisteredUserException
import kr.hailor.hailor.repository.AgreedTermsRepository
import kr.hailor.hailor.repository.TermsRepository
import kr.hailor.hailor.repository.UserRepository
import kr.hailor.hailor.util.JwtUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val oAuthProperties: OAuthProperties,
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
    private val termsRepository: TermsRepository,
    private val agreedTermsRepository: AgreedTermsRepository,
) {
    @Transactional
    fun login(request: LoginRequest): ServiceTokensResponse {
        val payload = verifyGoogleToken(request.token)
        val email = payload["email"] as String
        val user =
            userRepository.findByEmail(email)
                ?: throw NotRegisteredUserException()
        return makeTokens(user)
    }

    @Transactional(readOnly = true)
    fun refreshToken(refreshToken: String): ServiceTokensResponse {
        require(
            jwtUtil.validateToken(jwtUtil.refreshKey, refreshToken) &&
                jwtUtil.validateCachedRefreshTokenRotateId(refreshToken),
        ) {
            throw IllegalArgumentException("유효하지 않은 토큰입니다")
        }
        val userId = jwtUtil.getUserId(jwtUtil.refreshKey, refreshToken)
        val user = userRepository.findById(userId).get()
        return makeTokens(user)
    }

    @Transactional
    fun signUp(request: SignUpRequest): User {
        val allTerms = termsRepository.findAll()
        require(request.agreedTerms.containsAll(allTerms.filter { it.isRequired }.map { it.id })) {
            throw NotAgreedAllRequiredTermsException()
        }

        val payload = verifyGoogleToken(request.token)
        val email = payload["email"] as String
        if (userRepository.existsByEmail(email)) {
            throw AlreadyRegisteredUserException()
        }
        val user =
            userRepository.save(
                User(
                    email = email,
                    name = payload["name"] as String,
                    role = Role.USER,
                    providerId = payload["sub"] as String,
                ),
            )

        agreedTermsRepository.saveAll(
            request.agreedTerms.map {
                AgreedTerms(
                    user = user,
                    terms = allTerms.first { term -> term.id == it },
                )
            },
        )
        return user
    }

    private fun verifyGoogleToken(idTokenString: String): Map<String, Any> {
        val verifier =
            GoogleIdTokenVerifier
                .Builder(NetHttpTransport(), GsonFactory())
                .setAudience(listOf(oAuthProperties.clientId))
                .build()

        val idToken = verifier.verify(idTokenString) ?: throw RuntimeException("Invalid ID token")
        return idToken.payload as Map<String, Any>
    }

    private fun makeTokens(user: User): ServiceTokensResponse {
        val accessToken = jwtUtil.generateAccessToken(user)
        val rotateId = jwtUtil.generateRotateId()
        val refreshToken = jwtUtil.generateRefreshToken(user.id, rotateId)
        jwtUtil.storeCachedRefreshTokenRotateId(user.id, rotateId)
        return ServiceTokensResponse(accessToken, refreshToken)
    }
}
