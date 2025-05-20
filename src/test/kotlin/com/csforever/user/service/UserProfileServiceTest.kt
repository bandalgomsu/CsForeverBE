package com.csforever.user.service

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.question.implement.QuestionFinder
import com.csforever.app.question.model.Question
import com.csforever.app.question.model.QuestionTag
import com.csforever.app.submission.implement.SubmissionCounter
import com.csforever.app.submission.implement.SubmissionFinder
import com.csforever.app.submission.model.Submission
import com.csforever.app.user.model.Position
import com.csforever.app.user.model.Role
import com.csforever.app.user.model.User
import com.csforever.app.user.service.UserProfileService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.LocalDateTime
import kotlin.test.assertEquals

class UserProfileServiceTest {

    private lateinit var submissionCounter: SubmissionCounter
    private lateinit var submissionFinder: SubmissionFinder
    private lateinit var questionFinder: QuestionFinder
    private lateinit var userProfileService: UserProfileService

    private val mockUser = User(
        id = 1L,
        email = "test@example.com",
        password = "securepassword",
        nickname = "tester",
        career = 3,
        role = Role.USER,
        position = Position.BACKEND
    )

    @BeforeEach
    fun setUp() {
        submissionCounter = mock(SubmissionCounter::class.java)
        submissionFinder = mock(SubmissionFinder::class.java)
        questionFinder = mock(QuestionFinder::class.java)

        userProfileService = UserProfileService(
            submissionCounter,
            submissionFinder,
            questionFinder
        )
    }

    @Test
    fun getUserProfileTest() = runBlocking {
        `when`(submissionCounter.countByUserIdAndIsCorrect(1L, true)).thenReturn(5)

        val result = userProfileService.getUserProfile(mockUser)

        assertEquals(1L, result.id)
        assertEquals("test@example.com", result.email)
        assertEquals("tester", result.nickname)
        assertEquals("백엔드", result.position)
        assertEquals(5, result.correctSubmissionCount)
    }

    @Test
    fun findUserProfileSubmissionPageTest() = runBlocking {
        val now = LocalDateTime.now()

        val submissions = listOf(
            Submission(
                id = 100L,
                userId = 1L,
                questionId = 10L,
                answer = "42",
                isCorrect = true,
                feedback = "Correct!",
                createdAt = now,
                updatedAt = now
            )
        )

        val submissionPage = PageResponse(
            results = submissions,
            totalPages = 1,
            totalElements = 1,
            currentPage = 1,
            pageSize = 5
        )

        val question = Question(
            id = 10L,
            question = "What is 6 x 7?",
            tag = QuestionTag.JAVA,
            bestAnswer = "test_best_answer",
        )

        `when`(submissionFinder.findPageByUserIdAndIsCorrect(1L, true, 5, 1)).thenReturn(submissionPage)
        `when`(questionFinder.findAllByIdIn(listOf(10L))).thenReturn(listOf(question))

        val result = userProfileService.findUserProfileSubmissionPage(
            user = mockUser,
            isCorrect = true,
            size = 5,
            page = 1
        )

        assertEquals(1, result.totalElements)
        val item = result.results.first()
        assertEquals(100L, item.submissionId)
        assertEquals("What is 6 x 7?", item.question)
        assertEquals(QuestionTag.JAVA.displayName, item.tag)
        assertEquals("42", item.answer)
        assertEquals("Correct!", item.feedback)
    }
}
