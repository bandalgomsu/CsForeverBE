package com.csforever.app.domain.term.implement

import com.csforever.app.common.llm.LLMClient
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.common.redis.RedisKey
import com.csforever.app.common.scope.CustomScope
import com.csforever.app.domain.term.dao.TermDao
import com.csforever.app.domain.term.model.LLMTermCreateCommand
import com.csforever.app.domain.term.model.Term
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class TermFinder(
    private val termDao: TermDao,
    private val redisClient: RedisClient,
    @param:Qualifier("hybridClient") private val hybridClient: LLMClient,
) {

    private val log = LoggerFactory.getLogger(TermFinder::class.java)

    suspend fun findOrCreateByTerm(term: String): Term {
        val normalTerm = term.trim().replace(" ", "").lowercase()

        val cacheKey = RedisKey.TERM_GET.createKey(normalTerm)

        redisClient.getData(
            cacheKey,
            Term::class.java
        )?.let {
            return it
        }

        termDao.findByTerm(normalTerm)?.let {
            redisClient.setData(cacheKey, it, 60 * 60)
            return it
        }

        val command = LLMTermCreateCommand(
            term = normalTerm,
        )

        val response = hybridClient.requestByCommand(command)

        if (response.definition.isNotBlank()) {
            CustomScope.fireAndForget.let {
                termDao.insert(
                    Term(
                        term = response.term,
                        definition = response.definition,
                    )
                )
            }
        }

        val termModel = Term(
            term = response.term,
            definition = response.definition.ifBlank {
                "옳바른 용어가 아닙니다."
            }
        )

        redisClient.setData(cacheKey, termModel, 60 * 60)

        return termModel
    }
}