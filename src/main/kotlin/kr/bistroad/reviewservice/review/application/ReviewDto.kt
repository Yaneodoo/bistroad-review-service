package kr.bistroad.reviewservice.review.application

import java.time.OffsetDateTime
import java.util.*
import kr.bistroad.reviewservice.review.domain.Photo as DomainPhoto
import kr.bistroad.reviewservice.review.domain.StoreItem as DomainStoreItem
import kr.bistroad.reviewservice.review.domain.User as DomainUser

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
        val item: StoreItem,
        val writer: User,
        val orderId: UUID,
        val contents: String,
        val stars: Int,
        val photo: Photo?,
        val timestamp: OffsetDateTime
    ) : ReviewDto {
        data class User(
            val id: UUID,
            val username: String,
            val fullName: String,
            val photo: Photo? = null
        ) {
            constructor(user: DomainUser) : this(
                id = user.id,
                username = user.username,
                fullName = user.fullName,
                photo = user.photo?.let(::Photo)
            )
        }

        data class StoreItem(
            val id: UUID,
            val name: String,
            val description: String,
            val price: Double
        ) {
            constructor(storeItem: DomainStoreItem) : this(
                id = storeItem.id,
                name = storeItem.name,
                description = storeItem.description,
                price = storeItem.price
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