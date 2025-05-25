package com.csforever.app.question.implement

import com.csforever.app.common.redis.RedisClient
import com.csforever.app.common.redis.RedisKey
import com.csforever.app.question.dao.QuestionDao
import org.springframework.stereotype.Component

@Component
class QuestionInserter(
    private val questionDao: QuestionDao,
    private val redisClient: RedisClient
) {

    suspend fun setAllCacheByTagToRedisSet(): Unit {
        val questions = questionDao.findAll()

        questions.forEach {
            redisClient.addDataToSet(RedisKey.QUESTION_TAG_SET.createKey(it.tag.name), it)
        }
    }
}