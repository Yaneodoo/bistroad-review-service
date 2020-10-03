package kr.bistroad.reviewservice.review.application

import java.util.*
import kr.bistroad.reviewservice.review.domain.ReviewedItem as DomainReviewedItem

interface ReviewDto {
    data class ForCreate(
        val id: UUID? = null,
        val storeId: UUID,
        val itemId: UUID,
        val writerId: UUID,
        val orderId: UUID,
        val contents: String,
        val stars: Int
    ) : ReviewDto

    data class ForUpdate(
        val contents: String?,
        val stars: Int?
    ) : ReviewDto

    data class ForResult(
        val id: UUID,
        val storeId: UUID,
        val item: ReviewedItem,
        val writer: Writer,
        val orderId: UUID,
        val contents: String,
        val stars: Int
    ) : ReviewDto {
        data class Writer(
            val id: UUID,
            val username: String,
            val fullName: String
        )

        data class ReviewedItem(
            val id: UUID,
            val name: String,
            val price: Double,
            val photoUri: String? = null
        ) {
            constructor(domain: DomainReviewedItem) : this(
                id = domain.id,
                name = domain.name,
                price = domain.price,
                photoUri = domain.photoUri
            )
        }
    }
}