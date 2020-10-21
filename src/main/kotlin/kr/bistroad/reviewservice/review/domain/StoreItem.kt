package kr.bistroad.reviewservice.review.domain

import java.util.*

data class StoreItem(
    val id: UUID,
    val name: String,
    val description: String,
    val price: Double
)