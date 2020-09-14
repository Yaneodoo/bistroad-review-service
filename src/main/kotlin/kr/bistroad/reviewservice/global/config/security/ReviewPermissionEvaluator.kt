package kr.bistroad.reviewservice.global.config.security

import kr.bistroad.reviewservice.review.application.ReviewService
import kr.bistroad.reviewservice.review.presentation.ReviewController
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class ReviewPermissionEvaluator(
    private val reviewService: ReviewService
) : PermissionEvaluator {
    override fun hasPermission(authentication: Authentication?, targetDomainObject: Any?, permission: Any?) =
        throw UnsupportedOperationException()

    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        if (authentication != null && targetType == "Review" && permission is String) {
            val userId = (authentication.principal as UserPrincipal).userId
            val pathIds = targetId as ReviewController.PathIds
            val review = reviewService.readReview(pathIds.storeId!!, pathIds.itemId!!, pathIds.itemId!!)

            when (permission) {
                "read" -> return true
                "write" -> return (review != null && review.writerId == userId)
            }
        }
        return false
    }
}