package kr.bistroad.reviewservice.review.infrastructure

import kr.bistroad.reviewservice.review.application.ReviewDto
import kr.bistroad.reviewservice.review.domain.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ReviewRepositoryCustom {
    fun search(storeId: UUID, itemId: UUID, dto: ReviewDto.SearchReq, pageable: Pageable): Page<Review>
}