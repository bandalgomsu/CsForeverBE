package com.csforever.app.common.mail

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.exception.CommonErrorCode
import org.apache.commons.validator.routines.EmailValidator

class MailValidator(
) {

    companion object {
        fun validateEmail(email: String) {
            if (!EmailValidator.getInstance().isValid(email)) {
                throw BusinessException(CommonErrorCode.INVALID_EMAIL)
            }
        }
    }
}