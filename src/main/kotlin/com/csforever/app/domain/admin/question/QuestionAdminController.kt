package com.csforever.app.domain.admin.question

import com.csforever.app.domain.user.auth.annotation.AuthorizationContext
import com.csforever.app.domain.user.auth.exception.AuthErrorCode
import com.csforever.app.domain.user.auth.model.UserAuthorizationContext
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.domain.user.profile.model.Role
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/questions")
class QuestionAdminController(
    private val questionAdminService: QuestionAdminService
) {

    @PutMapping("/cache")
    suspend fun reloadAllCacheByTagToRedisSet(@AuthorizationContext context: UserAuthorizationContext): ResponseEntity<Unit> {
        if (context.user!!.role != Role.ADMIN) {
            throw BusinessException(AuthErrorCode.NOT_MATCHED_ROLE)
        }

        questionAdminService.reloadByTagToRedisSet()

        return ResponseEntity.ok().build()
    }
}