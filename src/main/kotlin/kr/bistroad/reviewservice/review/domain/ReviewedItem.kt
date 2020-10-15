package kr.bistroad.reviewservice.review.domain

import java.util.*

data class ReviewedItem(
    val id: UUID,
    val name: String,
    val description: String,
    val price: Double
)