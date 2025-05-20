package com.csforever.user.dao

import com.csforever.app.user.model.Position
import com.csforever.app.user.model.Role
import com.csforever.app.user.model.User
import com.csforever.infrastructure.database.user.UserCoroutineRepository
import com.csforever.infrastructure.database.user.UserEntity
import com.csforever.infrastructure.database.user.UserEntityRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ActiveProfiles("test")
@DataR2dbcTest
class UserEntityRepositoryTest(
    @Autowired
    private val userCoroutineRepository: UserCoroutineRepository
) {

    private lateinit var repository: UserEntityRepository
    private lateinit var user: User

    @BeforeEach
    fun setUp() = runBlocking {
        repository = UserEntityRepository(userCoroutineRepository)

        userCoroutineRepository.deleteAll()

        val entity = userCoroutineRepository.save(
            UserEntity(
                email = "test@example.com",
                password = "test1234",
                nickname = "test_user",
                career = 1,
                role = Role.USER,
                position = Position.BACKEND
            )
        )

        user = entity.toModel()
    }

    @Test
    fun insertTest() = runBlocking {
        val newUser = User(
            email = "new@example.com",
            password = "pass123",
            nickname = "newbie",
            career = 2,
            role = Role.USER,
            position = Position.FRONTEND
        )

        val saved = repository.insert(newUser)

        assertNotNull(saved.id)
        assertEquals(newUser.email, saved.email)
        assertEquals(newUser.nickname, saved.nickname)
        assertEquals(newUser.position, saved.position)
    }

    @Test
    fun findByIdTest() = runBlocking {
        val found = repository.findById(user.id!!)
        assertNotNull(found)
        assertEquals(user.email, found?.email)
    }

    @Test
    fun findByEmailTest() = runBlocking {
        val found = repository.findByEmail(user.email)
        assertNotNull(found)
        assertEquals(user.nickname, found?.nickname)
    }

    @Test
    fun existsByEmailTest() = runBlocking {
        val exists = repository.existsByEmail(user.email)
        val notExists = repository.existsByEmail("nope@example.com")

        assertTrue(exists)
        assertTrue(!notExists)
    }
}
