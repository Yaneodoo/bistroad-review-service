package kr.bistroad.reviewservice.review.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
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
    var photoUri: String? = null
)