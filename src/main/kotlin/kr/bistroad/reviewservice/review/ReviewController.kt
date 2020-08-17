package kr.bistroad.reviewservice.review

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ReviewController(
    private val reviewService: ReviewService
) {
    @GetMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    fun getReview(
        pathIds: PathIds
    ) = reviewService.readReview(pathIds.storeId!!, pathIds.itemId!!, pathIds.reviewId!!)

    @GetMapping("/stores/{storeId}/items/{itemId}/reviews")
    fun getReviews(
        @PathVariable storeId: UUID,
        @PathVariable itemId: UUID
    ) = reviewService.searchReviews(storeId, itemId)

    @PostMapping("/stores/{storeId}/items/{itemId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    fun postReview(
        @PathVariable storeId: UUID,
        @PathVariable itemId: UUID,
        @RequestBody dto: ReviewDto.CreateReq
    ) = reviewService.createReview(storeId, itemId, dto)

    @PatchMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    fun patchReview(
        pathIds: PathIds,
        @RequestBody dto: ReviewDto.PatchReq
    ) = reviewService.patchReview(pathIds.storeId!!, pathIds.itemId!!, pathIds.reviewId!!, dto)

    @DeleteMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    fun deleteReview(
        pathIds: PathIds
    ): ResponseEntity<Void> =
        if (reviewService.deleteReview(pathIds.storeId!!, pathIds.itemId!!, pathIds.reviewId!!))
            ResponseEntity.noContent().build()
        else
            ResponseEntity.notFound().build()

    data class PathIds(
        var storeId: UUID?,
        var itemId: UUID?,
        var reviewId: UUID?
    )
}