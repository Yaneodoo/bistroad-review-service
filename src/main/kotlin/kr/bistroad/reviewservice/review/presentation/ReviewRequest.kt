package kr.bistroad.reviewservice.review.presentation

import kr.bistroad.reviewservice.review.application.ReviewDto
import java.util.*

interface ReviewRequest {
    data class SearchParams(
        val storeId: UUID?,
        val itemId: UUID?,
        val writerId: UUID?,
        val orderId: UUID?
    )

    data class PostBody(
        val storeId: UUID,
        val itemId: UUID,
        val writerId: UUID,
        val orderId: UUID,
        val contents: String,
        val stars: Int
    ) {
        fun toDtoForCreate() = ReviewDto.ForCreate(
            storeId = storeId,
            itemId = itemId,
            writerId = writerId,
            orderId = orderId,
            contents = contents,
            stars = stars
        )
    }

    data class PatchBody(
        val contents: String?,
        val stars: Int?
    ) {
        fun toDtoForUpdate() = ReviewDto.ForUpdate(
            contents = contents,
            stars = stars
        )
    }
}