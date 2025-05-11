package com.csforever.app.common.pagination

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

class PageResponse<T>(
    @JsonProperty("results") val results: List<T>,
    @JsonProperty("totalPages") val totalPages: Long,
    @JsonProperty("totalElements") val totalElements: Long,
    @JsonProperty("currentPage") val currentPage: Int,
    @JsonProperty("pageSize") val pageSize: Int,
) : Serializable {
}