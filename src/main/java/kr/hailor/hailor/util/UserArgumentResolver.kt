package kr.hailor.hailor.util

import kr.hailor.hailor.enity.User
import kr.hailor.hailor.exception.AuthenticatedUserNotFoundException
import kr.hailor.hailor.service.SecurityUser
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter) = User::class.java.isAssignableFrom(parameter.parameterType)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): User {
        val authentication = SecurityContextHolder.getContext().authentication

        return if (authentication.isAuthenticated && authentication.principal is SecurityUser) {
            (authentication.principal as SecurityUser).user
        } else {
            throw AuthenticatedUserNotFoundException()
        }
    }
}
