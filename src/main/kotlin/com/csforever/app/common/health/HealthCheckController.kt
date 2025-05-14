package com.csforever.app.common.health

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @Hidden
    @GetMapping("/")
    suspend fun healthCheck(): String {
        return "OK"
    }
}