package kr.bistroad.reviewservice.review

import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ReviewController(
        private val reviewService: ReviewService
) {
    @GetMapping("/stores/{storeId}/items/{itemId}/reviews/{id}")
    fun getReview(
            @PathVariable storeId: UUID,
            @PathVariable itemId: UUID,
            @PathVariable id: UUID
    ) = reviewService.readReview(storeId, itemId, id)

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

    @PatchMapping("/stores/{storeId}/items/{itemId}/reviews/{id}")
    fun patchReview(
            @PathVariable storeId: UUID,
            @PathVariable itemId: UUID,
            @PathVariable id: UUID,
            @RequestBody dto: ReviewDto.PatchReq
    ) = reviewService.patchReview(storeId, itemId, id, dto)

    @DeleteMapping("/stores/{storeId}/items/{itemId}/reviews/{id}")
    fun deleteReview(
            @PathVariable storeId: UUID,
            @PathVariable itemId: UUID,
            @PathVariable id: UUID
    ) = reviewService.deleteReview(storeId, itemId, id)
}