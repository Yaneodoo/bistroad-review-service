package kr.bistroad.reviewservice.review.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("reviews")
data class Review(
    @Id
    val id: UUID = UUID.randomUUID(),

    val storeId: UUID,
    val itemId: UUID,
    var writerId: UUID,
    var orderId: UUID,
    var contents: String,
    var stars: Int
)