package com.csforever.infrastructure.database.user

import com.csforever.app.domain.user.profile.model.Position
import com.csforever.app.domain.user.profile.model.Role
import com.csforever.app.domain.user.profile.model.User
import com.csforever.infrastructure.database.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("users")
class UserEntity(
    @Id
    val id: Long? = null,
    @Column("email")
    val email: String,
    @Column("password")
    val password: String,
    @Column("nickname")
    val nickname: String,
    @Column("role")
    val role: Role,
    @Column("career")
    val career: Int,
    @Column("position")
    val position: Position,
) : BaseEntity() {

    fun toModel(): User {
        return User(
            id = id,
            email = email,
            password = password,
            nickname = nickname,
            career = career,
            position = position,
            role = role,
        )
    }
}