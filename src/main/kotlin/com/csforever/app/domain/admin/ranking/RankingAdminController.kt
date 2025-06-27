package com.csforever.app.domain.admin.ranking

import com.csforever.app.domain.user.auth.annotation.AuthorizationContext
import com.csforever.app.domain.user.auth.exception.AuthErrorCode
import com.csforever.app.domain.user.auth.model.UserAuthorizationContext
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.domain.user.profile.model.Role
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class RankingAdminController(
    @Value("\${scheduler.key}")
    private val schedulerKey: String,
    private val rankingAdminService: RankingAdminService
) {

    @PutMapping("/admin/ranking/analyze")
    suspend fun analyzeRankingAdmin(@AuthorizationContext context: UserAuthorizationContext): ResponseEntity<Unit> {
        if (context.user!!.role != Role.ADMIN) {
            throw BusinessException(AuthErrorCode.NOT_MATCHED_ROLE)
        }

        rankingAdminService.analyzeRanking()

        return ResponseEntity.ok().build()
    }

    @PutMapping("/scheduler/ranking/analyze")
    suspend fun analyzeRankingScheduler(@RequestParam schedulerApiKey: String): ResponseEntity<Unit> {
        if (schedulerApiKey != schedulerKey) {
            throw BusinessException(AuthErrorCode.NOT_MATCHED_ROLE)
        }

        rankingAdminService.analyzeRanking()

        return ResponseEntity.ok().build()
    }
}