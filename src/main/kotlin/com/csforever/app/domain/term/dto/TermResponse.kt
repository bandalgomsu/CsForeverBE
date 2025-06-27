package com.csforever.app.domain.term.dto

class TermResponse {
    data class TermInfo(
        val term: String,
        val definition: String
    )


    data class TermCreateResponse(
        val term: String,
        val definition: String
    )
}