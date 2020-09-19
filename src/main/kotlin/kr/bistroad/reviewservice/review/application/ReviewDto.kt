package kr.bistroad.reviewservice.review.application

import kr.bistroad.reviewservice.global.error.exception.WriterNotFoundException
import kr.bistroad.reviewservice.review.domain.Review
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.util.*

interface ReviewDto {
    data class CreateReq(
        val storeId: UUID,
        val itemId: UUID,
        val writerId: UUID,
        val orderId: UUID,
        val contents: String,
        val stars: Int
    )

    data class SearchReq(
        val storeId: UUID?,
        val itemId: UUID?,
        val writerId: UUID?,
        val orderId: UUID?
    )

    data class PatchReq(
        val contents: String?,
        val stars: Int?
    )

    data class CruRes(
        val id: UUID,
        val storeId: UUID,
        val itemId: UUID,
        val writer: Writer,
        val orderId: UUID,
        val contents: String,
        val stars: Int
    ) {
        companion object {
            private val restTemplate: RestTemplate = RestTemplate()

            fun fromEntity(review: Review) = CruRes(
                id = review.id!!,
                storeId = review.storeId,
                itemId = review.itemId,
                writer = fetchWriter(review.writerId),
                orderId = review.orderId,
                contents = review.contents,
                stars = review.stars
            )

            private fun fetchWriter(writerId: UUID) = try {
                restTemplate.getForObject(
                    "http://user-service:8080/users/$writerId",
                    Writer::class.java
                ) ?: throw WriterNotFoundException()
            } catch (ex: HttpClientErrorException.NotFound) {
                throw WriterNotFoundException(ex)
            }
        }

        data class Writer(
            val id: UUID,
            val username: String,
            val fullName: String
        )
    }
}