package com.csforever.app.common.exception

interface ErrorCode {

    fun getCodeValue(): String
    fun getStatusValue(): Int
    fun getMessageValue(): String
}