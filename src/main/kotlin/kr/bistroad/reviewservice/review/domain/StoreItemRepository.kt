package kr.bistroad.reviewservice.review.domain

import java.util.*

interface StoreItemRepository {
    fun findById(storeId: UUID, itemId: UUID): StoreItem?
}