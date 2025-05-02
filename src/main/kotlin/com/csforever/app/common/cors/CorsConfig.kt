package com.csforever.app.common.cors

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig(
    @Value("\${cors.allowed-origins}")
    private val allowedOrigins: List<String>
) {
    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val corsConfig = CorsConfiguration()
        corsConfig.allowCredentials = true
        allowedOrigins.forEach {
            corsConfig.addAllowedOrigin(it)
        }
        corsConfig.addAllowedHeader("*")
        corsConfig.addAllowedMethod("*") // GET, POST, PUT 등

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)

        return CorsWebFilter(source)
    }
}