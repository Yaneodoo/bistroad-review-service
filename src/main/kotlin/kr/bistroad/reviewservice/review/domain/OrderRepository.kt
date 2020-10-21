package kr.bistroad.reviewservice.review.domain

interface OrderRepository {
    fun addReview(review: Review)
    fun removeReview(review: Review)
}