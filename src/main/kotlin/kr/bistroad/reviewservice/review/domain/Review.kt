package kr.bistroad.reviewservice.review.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.OffsetDateTime
import java.util.*

@Document("reviews")
data class Review(
    @Id
    val id: UUID = UUID.randomUUID(),

    val store: Store,
    val order: Order,
    val item: ReviewedItem,
    val writer: Writer,
    var contents: String,
    var stars: Int,
    var photo: Photo? = null,
    val timestamp: OffsetDateTime = OffsetDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Review

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}