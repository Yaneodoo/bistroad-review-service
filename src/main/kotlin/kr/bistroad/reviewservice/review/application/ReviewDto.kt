package kr.bistroad.reviewservice.review.application

import java.util.*

interface ReviewDto {
    data class ForCreate(
        val id: UUID? = null,
        val storeId: UUID,
        val itemId: UUID,
        val writerId: UUID,
        val orderId: UUID,
        val contents: String,
        val stars: Int
    ) : ReviewDto

    data class ForUpdate(
        val contents: String?,
        val stars: Int?
    ) : ReviewDto

    data class ForResult(
        val id: UUID,
        val storeId: UUID,
        val itemId: UUID,
        val writer: Writer,
        val orderId: UUID,
        val contents: String,
        val stars: Int
    ) : ReviewDto {
        data class Writer(
            val id: UUID,
            val username: String,
            val fullName: String
        )
    }
}