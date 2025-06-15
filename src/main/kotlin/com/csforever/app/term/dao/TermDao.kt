package com.csforever.app.term.dao

import com.csforever.app.term.model.Term

interface TermDao {
    suspend fun findByTerm(term: String): Term?
    suspend fun insert(term: Term): Term
}