package kr.bistroad.reviewservice.review.infrastructure

import kr.bistroad.reviewservice.review.domain.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface ReviewRepository : JpaRepository<Review, UUID>, ReviewRepositoryCustom {
    @Transactional
    fun removeById(id: UUID): Long
}