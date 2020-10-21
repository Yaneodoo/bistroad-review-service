package kr.bistroad.reviewservice.review.domain

import java.util.*

data class User(
    val id: UUID,
    val username: String,
    val fullName: String,
    val photo: Photo? = null
)