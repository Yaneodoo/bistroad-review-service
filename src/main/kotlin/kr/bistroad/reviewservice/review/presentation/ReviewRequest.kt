package kr.bistroad.reviewservice.review.presentation

import kr.bistroad.reviewservice.global.error.exception.InvalidDateFormatException
import kr.bistroad.reviewservice.review.application.ReviewDto
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

interface ReviewRequest {
    data class SearchParams(
        val storeId: UUID? = null,
        val itemId: UUID? = null,
        val writerId: UUID? = null,
        val orderId: UUID? = null,
        val fetch: List<String>? = null
    )

    data class PostBody(
        val storeId: UUID,
        val itemId: UUID,
        val writerId: UUID,
        val orderId: UUID,
        val contents: String,
        val stars: Int,
        val timestamp: String? = null
    ) {
        fun toDtoForCreate() = ReviewDto.ForCreate(
            storeId = storeId,
            itemId = itemId,
            writerId = writerId,
            orderId = orderId,
            contents = contents,
            stars = stars,
            timestamp = timestamp?.let {
                try {
                    OffsetDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
                } catch (ex: DateTimeParseException) {
                    throw InvalidDateFormatException(ex)
                }
            } ?: OffsetDateTime.now()
        )
    }

    data class PatchBody(
        val contents: String? = null,
        val stars: Int? = null
    ) {
        fun toDtoForUpdate() = ReviewDto.ForUpdate(
            contents = contents,
            stars = stars
        )
    }
}