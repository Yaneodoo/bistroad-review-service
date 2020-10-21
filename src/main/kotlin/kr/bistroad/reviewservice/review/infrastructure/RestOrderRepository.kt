package kr.bistroad.reviewservice.review.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kr.bistroad.reviewservice.review.domain.Order
import kr.bistroad.reviewservice.review.domain.OrderRepository
import kr.bistroad.reviewservice.review.domain.Review
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import java.util.*

@Component
class RestOrderRepository(
    private val restTemplate: RestTemplate
) : OrderRepository {
    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    override fun addReview(review: Review) {
        val body = AddReviewBody(reviewId = review.id)

        val headers = HttpHeaders().apply { this["Authorization-Role"] = "ROLE_ADMIN" }
        val request = HttpEntity(objectMapper.writeValueAsString(body), headers)

        restTemplate.postForObject<Order>(
            "http://order-service:8080/orders/${review.order.id}/add-review",
            request
        )
    }

    override fun removeReview(review: Review) {
        val body = RemoveReviewBody(reviewId = review.id)

        val headers = HttpHeaders().apply { this["Authorization-Role"] = "ROLE_ADMIN" }
        val request = HttpEntity(objectMapper.writeValueAsString(body), headers)

        restTemplate.postForObject<Order>(
            "http://order-service:8080/orders/${review.order.id}/remove-review",
            request
        )
    }

    data class AddReviewBody(val reviewId: UUID)
    data class RemoveReviewBody(val reviewId: UUID)
}