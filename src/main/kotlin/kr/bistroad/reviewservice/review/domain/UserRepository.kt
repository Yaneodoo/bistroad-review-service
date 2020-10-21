package kr.bistroad.reviewservice.review.domain

import java.util.*

interface UserRepository {
    fun findById(id: UUID): User?
}