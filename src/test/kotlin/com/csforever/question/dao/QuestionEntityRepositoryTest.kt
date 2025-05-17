package com.csforever.question.dao

import com.csforever.app.question.model.QuestionTag
import com.csforever.infrastructure.database.question.QuestionCoroutineRepository
import com.csforever.infrastructure.database.question.QuestionEntity
import com.csforever.infrastructure.database.question.QuestionEntityRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DataR2dbcTest
class QuestionEntityRepositoryTest(
    @Autowired
    private val questionCoroutineRepository: QuestionCoroutineRepository
) {

    private val questionEntityRepository = QuestionEntityRepository(questionCoroutineRepository)

    var id1: Long = 1L
    var id2: Long = 1L
    val question = "test_question"
    val bestAnswer = "test_best_answer"
    val tag1 = QuestionTag.C_SHARP
    val tag2 = QuestionTag.JAVA

    val questionEntity1 = QuestionEntity(
        question = question,
        bestAnswer = bestAnswer,
        tag = tag1
    )

    val questionEntity2 = QuestionEntity(
        question = question,
        bestAnswer = bestAnswer,
        tag = tag2
    )

    @BeforeEach
    fun setUp() = runTest {
        questionCoroutineRepository.deleteAll()

        id1 = questionCoroutineRepository.save(questionEntity1).id!!
        id2 = questionCoroutineRepository.save(questionEntity2).id!!
    }

    @Test
    fun findByIdTest() = runTest {
        val entity = questionEntityRepository.findById(id1)

        assert(entity != null)
        assert(entity?.id == id1)
        assert(entity?.question == question)
        assert(entity?.bestAnswer == bestAnswer)
        assert(entity?.tag == tag2)
    }

    @Test
    fun findRandomByTag() = runTest {
        val entity = questionEntityRepository.findRandomByTag(listOf(tag1, tag2))

        assert(entity != null)
        assert(entity?.id == id1 || entity?.id == id2)
        assert(entity?.question == question)
        assert(entity?.bestAnswer == bestAnswer)
        assert(entity?.tag == tag1 || entity?.tag == tag2)
    }

    @Test
    fun findAllByIdIn() = runTest {
        val entities = questionEntityRepository.findAllByIdIn(listOf(id1, id2))

        assert(entities.size == 2)
        assert(entities[0].id == id1 || entities[0].id == id2)
        assert(entities[1].id == id1 || entities[1].id == id2)
        assert(entities[0].question == question)
        assert(entities[0].bestAnswer == bestAnswer)
        assert(entities[1].question == question)
        assert(entities[1].bestAnswer == bestAnswer)
        assert(entities[0].tag == tag1 || entities[0].tag == tag2)
        assert(entities[1].tag == tag1 || entities[1].tag == tag2)
    }
}