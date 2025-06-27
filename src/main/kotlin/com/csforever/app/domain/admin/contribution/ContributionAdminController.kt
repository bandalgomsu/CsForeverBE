package com.csforever.app.domain.admin.contribution

import com.csforever.app.domain.user.auth.exception.AuthErrorCode
import com.csforever.app.common.exception.BusinessException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1")
class ContributionAdminController(
    @Value("\${scheduler.key}")
    private val schedulerKey: String,
    private val contributionAdminService: ContributionAdminService,
) {

    @PutMapping("/scheduler/contributions/analyze")
    suspend fun analyzeContributionAdmin(
        @RequestParam schedulerApiKey: String,
        @RequestParam startDate: LocalDate = LocalDate.now().minusDays(1),
        @RequestParam endDate: LocalDate = LocalDate.now()
    ): ResponseEntity<Unit> {
        if (schedulerApiKey != schedulerKey) {
            throw BusinessException(AuthErrorCode.NOT_MATCHED_ROLE)
        }
        contributionAdminService.analyzeContribution(
            startDate.atStartOfDay().toLocalDate(),
            endDate.atStartOfDay().toLocalDate()
        )

        return ResponseEntity.ok().build()
    }
}