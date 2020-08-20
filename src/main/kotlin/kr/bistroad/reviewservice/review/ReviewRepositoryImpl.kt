package kr.bistroad.reviewservice.review

import com.querydsl.core.BooleanBuilder
import kr.bistroad.reviewservice.review.QReview.review
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReviewRepositoryImpl : QuerydslRepositorySupport(Review::class.java), ReviewRepositoryCustom {
    override fun search(storeId: UUID, itemId: UUID, dto: ReviewDto.SearchReq, pageable: Pageable): Page<Review> {
        val booleanBuilder = BooleanBuilder()
            .and(review.storeId.eq(storeId))
            .and(review.itemId.eq(itemId))
        if (dto.orderId != null) booleanBuilder.and(review.orderId.eq(dto.orderId))
        if (dto.writerId != null) booleanBuilder.and(review.writerId.eq(dto.writerId))

        val query = from(review)
            .where(booleanBuilder)

        val list = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(list, pageable, query.fetchCount())
    }
}