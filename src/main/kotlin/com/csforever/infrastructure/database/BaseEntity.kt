package com.csforever.infrastructure.database

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

abstract class BaseEntity(
    @CreatedDate
    open var createdAt: LocalDateTime? = null,
    @LastModifiedDate
    open var updatedAt: LocalDateTime? = null,
)