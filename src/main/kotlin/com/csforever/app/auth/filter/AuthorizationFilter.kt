package com.csforever.app.auth.filter

import com.csforever.app.auth.exception.AuthErrorCode
import com.csforever.app.auth.implement.TokenHandler
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.exception.CommonErrorCode
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class AuthorizationFilter(
    private val tokenHandler: TokenHandler,
    @Value("\${auth.paths}")
    private val authPaths: List<String>
) : WebFilter {

    private val logger = LoggerFactory.getLogger(AuthorizationFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return mono {
            val request = exchange.request

            if (request.method.name() == "OPTIONS") {
                return@mono chain.filter(exchange).awaitSingleOrNull()
            }

            val isAuthRequest = authPaths.any { it in request.path.value() }

            val token = request.headers.getFirst("Authorization")
                ?.removePrefix("Bearer ")
                ?.trim()
                ?: ""

            val authorization = tokenHandler.extractToken(token)

            if (isAuthRequest) {
                authorization.user ?: return@mono handleAuthException(
                    exchange,
                    BusinessException(AuthErrorCode.NOT_LOGIN_USER)
                ).awaitSingleOrNull()

                exchange.attributes["authorization"] = authorization

                return@mono chain.filter(exchange).awaitSingleOrNull()
            }

            exchange.attributes["authorization"] = authorization

            return@mono chain.filter(exchange).awaitSingleOrNull()
        }
    }

    fun handleAuthException(
        exchange: ServerWebExchange,
        exception: Exception
    ): Mono<Void> {
        return mono {
            val errorCode = if (exception is BusinessException) {
                exception.errorCode
            } else {
                CommonErrorCode.INTERNAL_SERVER_ERROR
            }

            exchange.response.statusCode = HttpStatusCode.valueOf(errorCode.getStatusValue())

            val requestOrigin = exchange.request.headers.getFirst("Origin")
            if (requestOrigin != null) {
                exchange.response.headers.add("Access-Control-Allow-Origin", requestOrigin)
                exchange.response.headers.add("Access-Control-Allow-Credentials", "true")
            }

            logger.error("${errorCode.getCodeValue()} - ${errorCode.getMessageValue()}", exception)

            val errorJson =
                """{ "code": "${errorCode.getCodeValue()}", "message": "${errorCode.getMessageValue()}", "status": ${errorCode.getStatusValue()} }"""
            val buffer = exchange.response.bufferFactory().wrap(errorJson.toByteArray())
            exchange.response.headers.contentType = MediaType.APPLICATION_JSON

            return@mono exchange.response.writeWith(Mono.just(buffer)).awaitSingleOrNull()
        }
    }
}