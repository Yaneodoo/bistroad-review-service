package kr.bistroad.reviewservice.review.infrastructure

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kr.bistroad.reviewservice.review.domain.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.repository.findByIdOrNull
import java.util.*

@DataMongoTest
internal class ReviewRepositoryTest {
    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @AfterEach
    fun clear() = reviewRepository.deleteAll()

    @Test
    fun `Saves a review`() {
        val review = Review(
            store = Store(UUID.randomUUID()),
            item = ReviewedItem(UUID.randomUUID(), "", 0.0),
            writer = Writer(UUID.randomUUID()),
            order = Order(UUID.randomUUID()),
            contents = "What a nice dish",
            stars = 5
        )
        reviewRepository.save(review)

        val foundReview = reviewRepository.findByIdOrNull(review.id)

        foundReview.shouldNotBeNull()
        foundReview.shouldBe(review)
    }

    @Test
    fun `Deletes a review`() {
        val review = Review(
            store = Store(UUID.randomUUID()),
            item = ReviewedItem(UUID.randomUUID(), "", 0.0),
            writer = Writer(UUID.randomUUID()),
            order = Order(UUID.randomUUID()),
            contents = "What a nice dish",
            stars = 5
        )
        reviewRepository.save(review)

        val reviewId = review.id
        val numDeleted = reviewRepository.removeById(reviewId)

        numDeleted.shouldBe(1)
        reviewRepository.findByIdOrNull(reviewId).shouldBeNull()
        reviewRepository.findAll().shouldBeEmpty()
    }
}