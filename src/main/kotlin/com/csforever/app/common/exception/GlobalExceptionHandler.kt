package com.csforever.app.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        exception: BusinessException,
    ): Mono<ResponseEntity<ErrorResponse>> {
        logger.error("${exception.errorCode.getCodeValue()} - ${exception.errorCode.getMessageValue()}", exception)

        val errorCode = exception.errorCode
        val response = ErrorResponse(errorCode)
        return Mono.just(ResponseEntity(response, HttpStatus.valueOf(errorCode.getStatusValue())))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        exception: Exception,
    ): Mono<ResponseEntity<ErrorResponse>> {
        logger.error("Exception", exception)

        val errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR
        val response = ErrorResponse(errorCode)
        return Mono.just(ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR))
    }
}
