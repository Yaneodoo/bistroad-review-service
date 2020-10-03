package kr.bistroad.reviewservice.review.infrastructure

import kr.bistroad.reviewservice.global.error.exception.WriterNotFoundException
import kr.bistroad.reviewservice.review.domain.ReviewedItem
import kr.bistroad.reviewservice.review.domain.ReviewedItemRepository
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.util.*

@Component
class RestReviewedItemRepository(
    private val restTemplate: RestTemplate
) : ReviewedItemRepository {
    override fun findById(storeId: UUID, itemId: UUID) =
        try {
            restTemplate.getForObject<ReviewedItem>(
                "http://store-service:8080/stores/$storeId/items/$itemId"
            )
        } catch (ex: HttpClientErrorException.NotFound) {
            throw WriterNotFoundException(ex)
        }
}