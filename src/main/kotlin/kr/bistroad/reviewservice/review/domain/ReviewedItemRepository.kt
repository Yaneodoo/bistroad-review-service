package kr.bistroad.reviewservice.review.domain

import java.util.*

interface ReviewedItemRepository {
    fun findById(storeId: UUID, itemId: UUID): ReviewedItem?
}