package com.csforever.app.user.dto

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.user.exception.UserErrorCode
import com.csforever.app.user.model.Position

class UserProfileRequest {
    data class UserProfileUpdateRequest(
        val nickname: String,
        val career: Int,
        val position: Position,
    ) {
        fun validate() {
            if (nickname.isBlank()) {
                throw BusinessException(UserErrorCode.USER_BLANK_NICKNAME)
            }

            if (nickname.length > 20) {
                throw BusinessException(UserErrorCode.USER_TOO_LONG_NICKNAME)
            }
        }
    }


}