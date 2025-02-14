package kr.hailor.hailor.repository

import kr.hailor.hailor.enity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User?
}
