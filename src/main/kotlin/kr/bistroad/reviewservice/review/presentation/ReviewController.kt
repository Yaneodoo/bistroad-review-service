package kr.bistroad.reviewservice.review.presentation

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.bistroad.reviewservice.global.error.exception.ReviewNotFoundException
import kr.bistroad.reviewservice.review.application.ReviewService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Api(tags = ["/reviews"])
class ReviewController(
    private val reviewService: ReviewService
) {
    @GetMapping("/reviews/{id}")
    @ApiOperation("\${swagger.doc.operation.review.get-review.description}")
    fun getReview(@PathVariable id: UUID) =
        reviewService.readReview(id) ?: throw ReviewNotFoundException()

    @GetMapping("/reviews")
    @ApiOperation("\${swagger.doc.operation.review.get-reviews.description}")
    fun getReviews(params: ReviewRequest.SearchParams, pageable: Pageable) =
        reviewService.searchReviews(
            storeId = params.storeId,
            itemId = params.itemId,
            writerId = params.writerId,
            orderId = params.orderId,
            pageable = pageable
        )

    @PostMapping("/reviews")
    @ApiOperation("\${swagger.doc.operation.review.post-review.description}")
    @PreAuthorize("isAuthenticated() and (( #dto.writerId == principal.userId ) or hasRole('ROLE_ADMIN'))")
    @ResponseStatus(HttpStatus.CREATED)
    fun postReview(@RequestBody body: ReviewRequest.PostBody) =
        reviewService.createReview(body.toDtoForCreate())

    @PatchMapping("/reviews/{id}")
    @ApiOperation("\${swagger.doc.operation.review.patch-review.description}")
    fun patchReview(@PathVariable id: UUID, @RequestBody body: ReviewRequest.PatchBody) =
        reviewService.patchReview(id, body.toDtoForUpdate())

    @DeleteMapping("/reviews/{id}")
    @ApiOperation("\${swagger.doc.operation.review.delete-review.description}")
    fun deleteReview(@PathVariable id: UUID) {
        val deleted = reviewService.deleteReview(id)
        if (!deleted) throw ReviewNotFoundException()
    }
}