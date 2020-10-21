package kr.bistroad.reviewservice.review.domain

import java.util.*

interface StoreItemRepository {
    fun findById(storeId: UUID, itemId: UUID): StoreItem?
    fun addReviewStar(storeId: UUID, itemId: UUID, review: Review)
    fun removeReviewStar(storeId: UUID, itemId: UUID, review: Review)
}