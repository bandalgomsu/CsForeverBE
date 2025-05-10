package com.csforever.app.auth.annotation

import com.csforever.app.auth.model.UserAuthorizationContext
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthorizationContextResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AuthorizationContext::class.java) &&
                parameter.parameterType == UserAuthorizationContext::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> {
        val authorization = exchange.attributes["authorization"] as UserAuthorizationContext

        return Mono.just(authorization)
    }
}