package com.csforever.app.domain.user.profile.model

enum class Position(val krName: String) {
    FRONTEND("프론트엔드"),
    BACKEND("백엔드"),
    FULLSTACK("풀스택"),
    ANDROID("안드로이드"),
    IOS("IOS"),
    GAME("게임"),
    AI("AI"),
    DATA_ENGINEER("데이터 엔지니어"),
    DEVOPS("DevOps"),
    DEFAULT("기타"),
    ;

    companion object {
        fun fromKrName(value: String): Position {
            return entries.firstOrNull { it.krName == value }
                ?: DEFAULT
        }
    }
}