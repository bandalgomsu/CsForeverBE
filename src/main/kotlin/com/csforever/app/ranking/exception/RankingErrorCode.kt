package com.csforever.app.ranking.exception

import com.csforever.app.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class RankingErrorCode(val code: String, val message: String, var status: Int) : ErrorCode {

    RANKING_NOT_FOUND("R01", "RANKING_NOT_FOUND", HttpStatus.NOT_FOUND.value()),
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