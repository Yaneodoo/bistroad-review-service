package kr.bistroad.reviewservice.review.application

import kr.bistroad.reviewservice.review.domain.Review
import kr.bistroad.reviewservice.review.domain.StoreItemRepository
import kr.bistroad.reviewservice.review.domain.UserRepository
import org.springframework.stereotype.Component

@Component
class ReviewMapper(
    private val storeItemRepository: StoreItemRepository,
    private val userRepository: UserRepository
) {
    fun mapToDtoForResult(review: Review): ReviewDto.ForResult {
        val item = storeItemRepository.findById(review.store.id, review.item.id)
            ?: throw IllegalStateException("Store item not found")
        val user = userRepository.findById(review.writer.id)
            ?: throw IllegalStateException("User not found")

        return ReviewDto.ForResult(
            id = review.id,
            storeId = review.store.id,
            item = ReviewDto.ForResult.StoreItem(item),
            writer = ReviewDto.ForResult.User(user),
            orderId = review.order.id,
            contents = review.contents,
            stars = review.stars,
            photo = review.photo?.let(ReviewDto.ForResult::Photo),
            timestamp = review.timestamp
        )
    }
}