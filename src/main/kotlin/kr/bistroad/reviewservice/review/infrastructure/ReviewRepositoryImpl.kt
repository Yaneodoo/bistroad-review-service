package kr.bistroad.reviewservice.review.infrastructure

import kr.bistroad.reviewservice.global.util.toPage
import kr.bistroad.reviewservice.review.domain.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReviewRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : ReviewRepositoryCustom {
    override fun search(
        storeId: UUID?,
        itemId: UUID?,
        writerId: UUID?,
        orderId: UUID?, pageable: Pageable
    ): Page<Review> {
        val query = Query().with(pageable)

        if (storeId != null) query.addCriteria(Criteria.where("store.id").`is`(storeId))
        if (itemId != null) query.addCriteria(Criteria.where("item.id").`is`(itemId))
        if (orderId != null) query.addCriteria(Criteria.where("order.id").`is`(orderId))
        if (writerId != null) query.addCriteria(Criteria.where("writer.id").`is`(writerId))

        return mongoTemplate.find<Review>(query).toPage(pageable)
    }
}