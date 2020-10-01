package kr.bistroad.reviewservice.review.infrastructure

import kr.bistroad.reviewservice.review.domain.Review
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ReviewRepository : MongoRepository<Review, UUID>, ReviewRepositoryCustom {
    fun removeById(id: UUID): Long
}