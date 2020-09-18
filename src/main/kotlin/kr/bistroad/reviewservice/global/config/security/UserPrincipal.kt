package kr.bistroad.reviewservice.global.config.security

import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

data class UserPrincipal(
    val userId: UUID?,
    val role: String?
) {
    val isAdmin: Boolean
        get() = role == "ROLE_ADMIN"

    companion object {
        fun ofCurrentContext() =
            SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }
}