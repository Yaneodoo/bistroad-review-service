package kr.bistroad.reviewservice.review

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ReviewRepositoryCustom {
    fun search(storeId: UUID, itemId: UUID, dto: ReviewDto.SearchReq, pageable: Pageable): Page<Review>
}