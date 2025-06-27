package com.csforever.app.domain.user.auth.annotation

import io.swagger.v3.oas.annotations.Hidden

@Hidden
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthorizationContext {
}