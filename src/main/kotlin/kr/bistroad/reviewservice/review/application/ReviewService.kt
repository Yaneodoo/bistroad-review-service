package kr.bistroad.reviewservice.review.application

import kr.bistroad.reviewservice.global.config.security.UserPrincipal
import kr.bistroad.reviewservice.global.error.exception.ReviewNotFoundException
import kr.bistroad.reviewservice.review.domain.Review
import kr.bistroad.reviewservice.review.infrastructure.ReviewRepository
import org.springframework.data.domain.Pageable
import org.springframework.security.access.AccessDeniedException
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

    fun readReview(storeId: UUID, itemId: UUID, id: UUID): ReviewDto.CruRes? {
        val review = reviewRepository.findByStoreIdAndItemIdAndId(storeId, itemId, id) ?: return null
        return ReviewDto.CruRes.fromEntity(review)
    }

    fun searchReviews(
        storeId: UUID,
        itemId: UUID,
        dto: ReviewDto.SearchReq,
        pageable: Pageable
    ): List<ReviewDto.CruRes> {
        return reviewRepository.search(storeId, itemId, dto, pageable)
            .content.map(ReviewDto.CruRes.Companion::fromEntity)
    }

    fun patchReview(storeId: UUID, itemId: UUID, id: UUID, dto: ReviewDto.PatchReq): ReviewDto.CruRes {
        val review =
            reviewRepository.findByStoreIdAndItemIdAndId(storeId, itemId, id) ?: throw ReviewNotFoundException()

        val principal = UserPrincipal.ofCurrentContext()
        if (principal.userId != review.writerId && !principal.isAdmin) throw AccessDeniedException("No permission")

        if (dto.contents != null) review.contents = dto.contents
        if (dto.stars != null) review.stars = dto.stars

        reviewRepository.save(review)
        return ReviewDto.CruRes.fromEntity(review)
    }

    fun deleteReview(storeId: UUID, itemId: UUID, id: UUID): Boolean {
        val review =
            reviewRepository.findByStoreIdAndItemIdAndId(storeId, itemId, id) ?: throw ReviewNotFoundException()
        val principal = UserPrincipal.ofCurrentContext()
        if (principal.userId != review.writerId && !principal.isAdmin) throw AccessDeniedException("No permission")

        val numDeleted = reviewRepository.removeByStoreIdAndItemIdAndId(storeId, itemId, id)
        return numDeleted > 0
    }
}