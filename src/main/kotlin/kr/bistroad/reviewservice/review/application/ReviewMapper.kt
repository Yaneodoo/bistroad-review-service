package kr.bistroad.reviewservice.review.application

import kr.bistroad.reviewservice.global.error.exception.WriterNotFoundException
import kr.bistroad.reviewservice.review.domain.Review
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.util.*

@Component
class ReviewMapper(
    private val restTemplate: RestTemplate
) {
    fun mapToDtoForResult(review: Review) = ReviewDto.ForResult(
        id = review.id,
        storeId = review.store.id,
        item = ReviewDto.ForResult.ReviewedItem(review.item),
        writer = fetchWriter(review.writer.id),
        orderId = review.order.id,
        contents = review.contents,
        stars = review.stars,
        photoUri = review.photoUri
    )

    private fun fetchWriter(writerId: UUID) =
        try {
            restTemplate.getForObject<ReviewDto.ForResult.Writer>(
                "http://user-service:8080/users/$writerId"
            )
        } catch (ex: HttpClientErrorException.NotFound) {
            throw WriterNotFoundException(ex)
        }
}