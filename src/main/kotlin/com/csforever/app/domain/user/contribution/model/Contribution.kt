package com.csforever.app.domain.user.contribution.model

import java.time.LocalDate
import java.time.LocalDateTime


class Contribution(
    val id: Long? = null,
    val date: LocalDate,
    val count: Int,
    val level: Int = if (count < 1) 0 else if (count < 5) 1 else if (count < 10) 2 else if (count < 20) 3 else 4,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
}