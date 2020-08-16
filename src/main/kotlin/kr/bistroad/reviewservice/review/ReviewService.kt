package kr.bistroad.reviewservice.review

import org.springframework.stereotype.Service
import java.util.*

@Service
class ReviewService(
        private val reviewRepository: ReviewRepository
) {
    fun createReview(storeId: UUID, itemId: UUID, dto: ReviewDto.CreateReq): ReviewDto.CruRes {
        val review = Review(
                storeId = storeId,
                itemId = itemId,
                writerId = dto.writerId,
                orderId = dto.orderId,
                contents = dto.contents,
                stars = dto.stars
        )
        reviewRepository.save(review)
        return ReviewDto.CruRes.fromEntity(review)
    }

    fun readReview(pathIds: ReviewDto.PathIds): ReviewDto.CruRes? {
        val review = reviewRepository.findByStoreIdAndItemIdAndId(
                pathIds.storeId, pathIds.itemId, pathIds.reviewId
        ) ?: return null
        return ReviewDto.CruRes.fromEntity(review)
    }

    fun searchReviews(storeId: UUID, itemId: UUID): List<ReviewDto.CruRes> {
        return reviewRepository.findAllByStoreIdAndItemId(storeId, itemId)
                .map(ReviewDto.CruRes.Companion::fromEntity)
    }

    fun patchReview(pathIds: ReviewDto.PathIds, dto: ReviewDto.PatchReq): ReviewDto.CruRes {
        val review = reviewRepository.findByStoreIdAndItemIdAndId(
                pathIds.storeId, pathIds.itemId, pathIds.reviewId
        ) ?: error("Review not found")

        if (dto.contents != null) review.contents = dto.contents
        if (dto.stars != null) review.stars = dto.stars

        reviewRepository.save(review)
        return ReviewDto.CruRes.fromEntity(review)
    }

    fun deleteReview(pathIds: ReviewDto.PathIds): Boolean {
        val numDeleted = reviewRepository.removeByStoreIdAndItemIdAndId(
                pathIds.storeId, pathIds.itemId, pathIds.reviewId
        )
        return numDeleted > 0
    }
}