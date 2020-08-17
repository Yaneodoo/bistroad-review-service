package kr.bistroad.reviewservice.review

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface ReviewRepository : JpaRepository<Review, UUID> {
    fun findByStoreIdAndItemIdAndId(storeId: UUID, itemId: UUID, id: UUID): Review?

    fun findAllByStoreIdAndItemId(storeId: UUID, itemId: UUID): List<Review>
    fun findAllByStoreIdAndItemIdAndWriterId(storeId: UUID, itemId: UUID, writerId: UUID): List<Review>
    fun findAllByStoreIdAndItemIdAndOrderId(storeId: UUID, itemId: UUID, orderId: UUID): List<Review>

    @Transactional
    fun removeByStoreIdAndItemIdAndId(storeId: UUID, itemId: UUID, id: UUID): Long
}