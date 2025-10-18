package com.csforever.app.domain.donation.exception

import com.csforever.app.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class DonationErrorCode(val code: String, val message: String, var status: Int) : ErrorCode {

    DONATION_NOT_FOUND("D01", "DONATION_NOT_FOUND", HttpStatus.NOT_FOUND.value()),
    ALREADY_CONFIRMED("D02", "ALREADY_CONFIRMED", HttpStatus.BAD_REQUEST.value())
    ;

    override fun getCodeValue(): String {
        return this.code
    }

    override fun getStatusValue(): Int {
        return this.status
    }

    override fun getMessageValue(): String {
        return this.message
    }
}