package com.csforever.infrastructure.database.term

import com.csforever.app.term.dao.TermDao
import com.csforever.app.term.model.Term
import org.springframework.stereotype.Repository

@Repository
class TermEntityRepository(
    private val termCoroutineRepository: TermCoroutineRepository
) : TermDao {

    override suspend fun findByTerm(term: String): Term? {
        return termCoroutineRepository.findByTerm(term)?.toModel()
    }

    override suspend fun insert(term: Term): Term {
        return termCoroutineRepository.save(
            TermEntity(
                id = term.id,
                term = term.term,
                definition = term.definition
            )
        ).toModel()
    }
}