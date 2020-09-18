package kr.bistroad.reviewservice.review.presentation

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.bistroad.reviewservice.global.error.exception.ReviewNotFoundException
import kr.bistroad.reviewservice.review.application.ReviewDto
import kr.bistroad.reviewservice.review.application.ReviewService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Api(tags = ["/stores/**/items/**/reviews"])
class ReviewController(
    private val reviewService: ReviewService
) {
    @GetMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    @ApiOperation("\${swagger.doc.operation.review.get-review.description}")
    fun getReview(
        @PathVariable storeId: UUID,
        @PathVariable itemId: UUID,
        @PathVariable reviewId: UUID
    ) = reviewService.readReview(storeId, itemId, reviewId)
        ?: throw ReviewNotFoundException()

    @GetMapping("/stores/{storeId}/items/{itemId}/reviews")
    @ApiOperation("\${swagger.doc.operation.review.get-reviews.description}")
    fun getReviews(
        @PathVariable storeId: UUID,
        @PathVariable itemId: UUID,
        dto: ReviewDto.SearchReq,
        pageable: Pageable
    ) = reviewService.searchReviews(storeId, itemId, dto, pageable)

    @PostMapping("/stores/{storeId}/items/{itemId}/reviews")
    @ApiOperation("\${swagger.doc.operation.review.post-review.description}")
    @PreAuthorize("isAuthenticated() and (( #dto.writerId == principal.userId ) or hasRole('ROLE_ADMIN'))")
    @ResponseStatus(HttpStatus.CREATED)
    fun postReview(
        @PathVariable storeId: UUID,
        @PathVariable itemId: UUID,
        @RequestBody dto: ReviewDto.CreateReq
    ) = reviewService.createReview(storeId, itemId, dto)

    @PatchMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    @ApiOperation("\${swagger.doc.operation.review.patch-review.description}")
    fun patchReview(
        @PathVariable storeId: UUID,
        @PathVariable itemId: UUID,
        @PathVariable reviewId: UUID,
        @RequestBody dto: ReviewDto.PatchReq
    ) = reviewService.patchReview(storeId, itemId, reviewId, dto)

    @DeleteMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    @ApiOperation("\${swagger.doc.operation.review.delete-review.description}")
    fun deleteReview(
        @PathVariable storeId: UUID,
        @PathVariable itemId: UUID,
        @PathVariable reviewId: UUID
    ): ResponseEntity<Void> =
        if (reviewService.deleteReview(storeId, itemId, reviewId))
            ResponseEntity.noContent().build()
        else
            ResponseEntity.notFound().build()
}