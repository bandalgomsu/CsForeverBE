package com.csforever.app.domain.term.dao

import com.csforever.app.domain.term.model.Term

interface TermDao {
    suspend fun findByTerm(term: String): Term?
    suspend fun insert(term: Term): Term
}