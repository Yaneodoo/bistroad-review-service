package kr.bistroad.reviewservice.review.application

import kr.bistroad.reviewservice.global.config.security.UserPrincipal
import kr.bistroad.reviewservice.global.error.exception.ReviewNotFoundException
import kr.bistroad.reviewservice.review.domain.*
import kr.bistroad.reviewservice.review.infrastructure.ReviewRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val storeItemRepository: StoreItemRepository,
    private val orderRepository: OrderRepository,
    private val reviewMapper: ReviewMapper
) {
    fun createReview(dto: ReviewDto.ForCreate): ReviewDto.ForResult {
        val review = Review(
            store = Store(dto.storeId),
            item = ReviewedItem(dto.itemId),
            writer = Writer(dto.writerId),
            order = Order(dto.orderId),
            contents = dto.contents,
            stars = dto.stars,
            timestamp = dto.timestamp
        )
        reviewRepository.save(review)

        storeItemRepository.addReviewStar(review.store.id, review.item.id, review)
        orderRepository.addReview(review)

        return reviewMapper.mapToDtoForResult(review)
    }

    fun readReview(id: UUID, fetchList: List<FetchTarget> = emptyList()): ReviewDto.ForResult? {
        val review = reviewRepository.findByIdOrNull(id) ?: return null
        return reviewMapper.mapToDtoForResult(review, fetchList)
    }

    fun searchReviews(
        storeId: UUID?,
        itemId: UUID?,
        writerId: UUID?,
        orderId: UUID?,
        pageable: Pageable,
        fetchList: List<FetchTarget> = emptyList()
    ): List<ReviewDto.ForResult> =
        reviewRepository.search(
            storeId = storeId,
            itemId = itemId,
            writerId = writerId,
            orderId = orderId,
            pageable = pageable
        ).content
            .map { reviewMapper.mapToDtoForResult(it, fetchList) }

    fun patchReview(id: UUID, dto: ReviewDto.ForUpdate): ReviewDto.ForResult {
        val review = reviewRepository.findByIdOrNull(id) ?: throw ReviewNotFoundException()

        val principal = UserPrincipal.ofCurrentContext()
        if (principal.userId != review.writer.id && !principal.isAdmin) throw AccessDeniedException("No permission")

        if (dto.contents != null) review.contents = dto.contents
        if (dto.stars != null) review.stars = dto.stars

        reviewRepository.save(review)
        return reviewMapper.mapToDtoForResult(review)
    }

    fun deleteReview(id: UUID): Boolean {
        val review = reviewRepository.findByIdOrNull(id) ?: throw ReviewNotFoundException()
        val principal = UserPrincipal.ofCurrentContext()
        if (principal.userId != review.writer.id && !principal.isAdmin) throw AccessDeniedException("No permission")

        storeItemRepository.removeReviewStar(review.store.id, review.item.id, review)
        orderRepository.removeReview(review)

        val numDeleted = reviewRepository.removeById(id)
        return numDeleted > 0
    }
}