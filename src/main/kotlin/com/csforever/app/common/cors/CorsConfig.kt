package com.csforever.app.common.cors

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val corsConfig = CorsConfiguration()
        corsConfig.allowCredentials = true
        corsConfig.addAllowedOriginPattern("*") // 또는 "http://localhost:3000" 같은 특정 도메인
        corsConfig.addAllowedHeader("*")
        corsConfig.addAllowedMethod("*") // GET, POST, PUT 등

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)

        return CorsWebFilter(source)
    }
}