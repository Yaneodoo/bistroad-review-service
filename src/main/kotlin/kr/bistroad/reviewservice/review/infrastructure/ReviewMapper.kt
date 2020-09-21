package kr.bistroad.reviewservice.review.infrastructure

import kr.bistroad.reviewservice.global.error.exception.WriterNotFoundException
import kr.bistroad.reviewservice.review.application.ReviewDto
import kr.bistroad.reviewservice.review.domain.Review
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.util.*

@Component
class ReviewMapper {
    private val restTemplate: RestTemplate = RestTemplate()

    fun mapToDtoForResult(review: Review) = ReviewDto.ForResult(
        id = review.id!!,
        storeId = review.storeId,
        itemId = review.itemId,
        writer = fetchWriter(review.writerId),
        orderId = review.orderId,
        contents = review.contents,
        stars = review.stars
    )

    private fun fetchWriter(writerId: UUID) =
        try {
            restTemplate.getForObject(
                "http://user-service:8080/users/$writerId",
                ReviewDto.ForResult.Writer::class.java
            ) ?: throw WriterNotFoundException()
        } catch (ex: HttpClientErrorException.NotFound) {
            throw WriterNotFoundException(ex)
        }
}