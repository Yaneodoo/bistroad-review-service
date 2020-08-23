package kr.bistroad.reviewservice.review

import org.springframework.security.access.prepost.PreAuthorize
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import kr.bistroad.reviewservice.exception.ReviewNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.Serializable
import java.util.*

@RestController
@Api(tags = ["/stores/**/items/**/reviews"])
class ReviewController(
    private val reviewService: ReviewService
) {
    @GetMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    @ApiOperation("\${swagger.doc.operation.review.get-review.description}")
    fun getReview(
        pathIds: PathIds
    ) = reviewService.readReview(pathIds.storeId!!, pathIds.itemId!!, pathIds.reviewId!!)
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
    @ApiImplicitParam(
        name = "Authorization", value = "Access Token", required = true, paramType = "header",
        allowEmptyValue = false, dataTypeClass = String::class, example = "Bearer access_token"
    )
    @PreAuthorize("isAuthenticated() and (( #dto.writerId == principal.userId ) or hasRole('ROLE_ADMIN'))")
    @ResponseStatus(HttpStatus.CREATED)
    fun postReview(
        @PathVariable storeId: UUID,
        @PathVariable itemId: UUID,
        @RequestBody dto: ReviewDto.CreateReq
    ) = reviewService.createReview(storeId, itemId, dto)

    @PatchMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    @ApiOperation("\${swagger.doc.operation.review.patch-review.description}")
    @ApiImplicitParam(
        name = "Authorization", value = "Access Token", required = true, paramType = "header",
        allowEmptyValue = false, dataTypeClass = String::class, example = "Bearer access_token"
    )
    @PreAuthorize("isAuthenticated() and (( hasPermission(#pathIds, 'Review', 'write') ) or hasRole('ROLE_ADMIN'))")
    fun patchReview(
        pathIds: PathIds,
        @RequestBody dto: ReviewDto.PatchReq
    ) = reviewService.patchReview(pathIds.storeId!!, pathIds.itemId!!, pathIds.reviewId!!, dto)

    @DeleteMapping("/stores/{storeId}/items/{itemId}/reviews/{reviewId}")
    @ApiOperation("\${swagger.doc.operation.review.delete-review.description}")
    @ApiImplicitParam(
        name = "Authorization", value = "Access Token", required = true, paramType = "header",
        allowEmptyValue = false, dataTypeClass = String::class, example = "Bearer access_token"
    )
    @PreAuthorize("isAuthenticated() and (( hasPermission(#pathIds, 'Review', 'write') ) or hasRole('ROLE_ADMIN'))")
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
    ) : Serializable
}