package com.csforever.submission.dao

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.submission.model.Submission
import com.csforever.infrastructure.database.submission.SubmissionCoroutineRepository
import com.csforever.infrastructure.database.submission.SubmissionEntity
import com.csforever.infrastructure.database.submission.SubmissionEntityRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ActiveProfiles("test")
@DataR2dbcTest
class SubmissionEntityRepositoryTest(
    @Autowired
    private val submissionCoroutineRepository: SubmissionCoroutineRepository
) {

    private lateinit var repository: SubmissionEntityRepository

    private val baseSubmission = Submission(
        userId = 1L,
        questionId = 1L,
        answer = "test_answer",
        isCorrect = true,
        feedback = "test_feedback"
    )

    @BeforeEach
    fun setUp() = runBlocking {
        repository = SubmissionEntityRepository(submissionCoroutineRepository)
        submissionCoroutineRepository.deleteAll()

        repeat(10) {
            submissionCoroutineRepository.save(
                SubmissionEntity(
                    userId = 1L,
                    questionId = it.toLong() + 1,
                    answer = "answer $it",
                    isCorrect = it % 2 == 0, // 짝수는 정답
                    feedback = "feedback $it"
                )
            )
        }
    }

    @Test
    @DisplayName("insert(): 제출 저장")
    fun insertTest() = runBlocking {
        val result = repository.insert(baseSubmission)

        assertNotNull(result.id)
        assertEquals(baseSubmission.userId, result.userId)
        assertEquals(baseSubmission.questionId, result.questionId)
        assertEquals(baseSubmission.answer, result.answer)
        assertEquals(baseSubmission.isCorrect, result.isCorrect)
        assertEquals(baseSubmission.feedback, result.feedback)

        submissionCoroutineRepository.deleteById(result.id!!)
    }

    @Test
    @DisplayName("countDistinctQuestionIdByUserIdAndCorrect(): 정답 제출 수")
    fun countDistinctCorrectSubmissionsTest() = runBlocking {
        val count = repository.countDistinctQuestionIdByUserIdAndCorrect(1L, true)
        assertEquals(5, count) // 0,2,4,6,8 → 총 5개 (distinct questionId)
    }

    @Test
    @DisplayName("findPageByUserIdAndIsCorrect(): 페이징 테스트")
    fun findPageByUserIdAndIsCorrectTest() = runBlocking {
        val page: PageResponse<Submission> = repository.findPageByUserIdAndIsCorrect(
            userId = 1L,
            isCorrect = false,
            size = 2,
            page = 1
        )

        assertEquals(2, page.results.size)
        assertEquals(2, page.pageSize)
        assertEquals(1, page.currentPage)
        assertEquals(3, page.totalPages) // 총 4개 중 2개씩 → 2페이지
        assertEquals(5, page.totalElements)
    }
}
