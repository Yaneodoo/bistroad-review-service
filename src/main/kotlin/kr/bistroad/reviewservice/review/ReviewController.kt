package kr.bistroad.reviewservice.review

import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ReviewController(
        private val reviewService: ReviewService
) {
    @GetMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    fun getReview(
            pathIds: ReviewDto.PathIds
    ) = reviewService.readReview(pathIds)

    @GetMapping("/stores/{storeId}/items/{itemId}/reviews")
    fun getReviews(
            @PathVariable storeId: UUID,
            @PathVariable itemId: UUID
    ) = reviewService.searchReviews(storeId, itemId)

    @PostMapping("/stores/{storeId}/items/{itemId}/reviews")
    fun postReview(
            @PathVariable storeId: UUID,
            @PathVariable itemId: UUID,
            @RequestBody dto: ReviewDto.CreateReq
    ) = reviewService.createReview(storeId, itemId, dto)

    @PatchMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    fun patchReview(
            pathIds: ReviewDto.PathIds,
            @RequestBody dto: ReviewDto.PatchReq
    ) = reviewService.patchReview(pathIds, dto)

    @DeleteMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    fun deleteReview(
            pathIds: ReviewDto.PathIds
    ) = reviewService.deleteReview(pathIds)
}