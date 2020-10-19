package kr.bistroad.reviewservice.review.application

import java.time.OffsetDateTime
import java.util.*
import kr.bistroad.reviewservice.review.domain.Photo as DomainPhoto
import kr.bistroad.reviewservice.review.domain.ReviewedItem as DomainReviewedItem

interface ReviewDto {
    data class ForCreate(
        val id: UUID? = null,
        val storeId: UUID,
        val itemId: UUID,
        val writerId: UUID,
        val orderId: UUID,
        val contents: String,
        val stars: Int,
        val timestamp: OffsetDateTime = OffsetDateTime.now()
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
        val stars: Int,
        val photo: Photo?,
        val timestamp: OffsetDateTime
    ) : ReviewDto {
        data class Writer(
            val id: UUID,
            val username: String,
            val fullName: String,
            val photo: Photo? = null
        )

        data class ReviewedItem(
            val id: UUID,
            val name: String,
            val description: String,
            val price: Double
        ) {
            constructor(domain: DomainReviewedItem) : this(
                id = domain.id,
                name = domain.name,
                description = domain.description,
                price = domain.price
            )
        }

        data class Photo(
            val sourceUrl: String,
            val thumbnailUrl: String
        ) {
            constructor(domain: DomainPhoto): this(domain.sourceUrl, domain.thumbnailUrl)
        }
    }
}