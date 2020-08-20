package kr.bistroad.reviewservice.review

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface ReviewRepository : JpaRepository<Review, UUID>, ReviewRepositoryCustom {
    fun findByStoreIdAndItemIdAndId(storeId: UUID, itemId: UUID, id: UUID): Review?

    @Transactional
    fun removeByStoreIdAndItemIdAndId(storeId: UUID, itemId: UUID, id: UUID): Long
}