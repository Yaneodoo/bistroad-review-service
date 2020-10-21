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
    fun mapToDtoForResult(review: Review, fetchList: List<FetchTarget> = emptyList()): ReviewDto.ForResult {
        val item = if (FetchTarget.STORE_ITEM in fetchList) {
            storeItemRepository.findById(review.store.id, review.item.id)
                ?.let { ReviewDto.ForResult.StoreItem(it) }
                ?: throw IllegalStateException("Store item not found")
        } else {
            ReviewDto.ForResult.StoreItem(id = review.id)
        }
        val user = userRepository.findById(review.writer.id)
            ?.let { ReviewDto.ForResult.User(it) }
            ?: throw IllegalStateException("User not found")

        return ReviewDto.ForResult(
            id = review.id,
            storeId = review.store.id,
            item = item,
            writer = user,
            orderId = review.order.id,
            contents = review.contents,
            stars = review.stars,
            photo = review.photo?.let(ReviewDto.ForResult::Photo),
            timestamp = review.timestamp
        )
    }
}