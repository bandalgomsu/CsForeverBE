package com.csforever.app.contribution.controller

import com.csforever.app.auth.annotation.AuthorizationContext
import com.csforever.app.auth.model.UserAuthorizationContext
import com.csforever.app.contribution.dto.ContributionQueryResponse
import com.csforever.app.contribution.service.ContributionQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1")
class ContributionQueryController(
    private val contributionQueryService: ContributionQueryService
) {

    @GetMapping("/contributions")
    suspend fun findAllByUserIdAndYearUntilToday(
        @AuthorizationContext authorizationContext: UserAuthorizationContext,
        @RequestParam year: Int = LocalDate.now().year
    ): ResponseEntity<ContributionQueryResponse.ContributionInfos> {
        val userId = authorizationContext.user!!.id!!

        val response = contributionQueryService.findAllByUserIdAndYearUntilToday(userId, year)

        return ResponseEntity.ok(response)
    }
}