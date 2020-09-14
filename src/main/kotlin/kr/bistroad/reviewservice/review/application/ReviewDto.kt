package kr.bistroad.reviewservice.review.application

import kr.bistroad.reviewservice.review.domain.Review
import java.util.*

interface ReviewDto {
    data class CreateReq(
        val writerId: UUID,
        val orderId: UUID,
        val contents: String,
        val stars: Int
    )

    data class SearchReq(
        val writerId: UUID?,
        val orderId: UUID?
    )

    data class PatchReq(
        val contents: String?,
        val stars: Int?
    )

    data class CruRes(
        val id: UUID,
        val writerId: UUID,
        val orderId: UUID,
        val contents: String,
        val stars: Int
    ) {
        companion object {
            fun fromEntity(review: Review) = CruRes(
                id = review.id!!,
                writerId = review.writerId,
                orderId = review.orderId,
                contents = review.contents,
                stars = review.stars
            )
        }
    }
}