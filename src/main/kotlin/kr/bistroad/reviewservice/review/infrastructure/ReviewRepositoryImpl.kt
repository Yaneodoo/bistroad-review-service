package kr.bistroad.reviewservice.review.infrastructure

import com.querydsl.core.BooleanBuilder
import kr.bistroad.reviewservice.review.domain.QReview.review
import kr.bistroad.reviewservice.review.domain.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReviewRepositoryImpl : QuerydslRepositorySupport(Review::class.java), ReviewRepositoryCustom {
    override fun search(
        storeId: UUID?,
        itemId: UUID?,
        writerId: UUID?,
        orderId: UUID?, pageable: Pageable
    ): Page<Review> {
        val booleanBuilder = BooleanBuilder()

        if (storeId != null) booleanBuilder.and(review.storeId.eq(storeId))
        if (itemId != null) booleanBuilder.and(review.itemId.eq(itemId))
        if (orderId != null) booleanBuilder.and(review.orderId.eq(orderId))
        if (writerId != null) booleanBuilder.and(review.writerId.eq(writerId))

        val query = from(review)
            .where(booleanBuilder)

        val list = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(list, pageable, query.fetchCount())
    }
}