package kr.bistroad.reviewservice.review.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kr.bistroad.reviewservice.review.domain.Review
import kr.bistroad.reviewservice.review.domain.StoreItem
import kr.bistroad.reviewservice.review.domain.StoreItemRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForObject
import java.util.*

@Component
class RestStoreItemRepository(
    private val restTemplate: RestTemplate
) : StoreItemRepository {
    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    override fun findById(storeId: UUID, itemId: UUID) =
        try {
            restTemplate.getForObject<StoreItem>(
                "http://store-service:8080/stores/$storeId/items/$itemId"
            )
        } catch (ex: HttpClientErrorException.NotFound) {
            null
        }

    override fun addReviewStar(storeId: UUID, itemId: UUID, review: Review) {
        val body = AddReviewStarBody(reviewId = review.id, stars = review.stars)

        val headers = HttpHeaders().apply {
            this.contentType = MediaType.APPLICATION_JSON
            this["Authorization-Role"] = "ROLE_ADMIN"
        }
        val request = HttpEntity(objectMapper.writeValueAsString(body), headers)

        restTemplate.postForObject<StoreItem>(
            "http://store-service:8080/stores/$storeId/items/$itemId/add-review-star",
            request
        )
    }

    override fun removeReviewStar(storeId: UUID, itemId: UUID, review: Review) {
        val body = RemoveReviewStarBody(reviewId = review.id)

        val headers = HttpHeaders().apply {
            this.contentType = MediaType.APPLICATION_JSON
            this["Authorization-Role"] = "ROLE_ADMIN"
        }
        val request = HttpEntity(objectMapper.writeValueAsString(body), headers)

        restTemplate.postForObject<StoreItem>(
            "http://store-service:8080/stores/$storeId/items/$itemId/remove-review-star",
            request
        )
    }

    data class AddReviewStarBody(val reviewId: UUID, val stars: Int)
    data class RemoveReviewStarBody(val reviewId: UUID)
}