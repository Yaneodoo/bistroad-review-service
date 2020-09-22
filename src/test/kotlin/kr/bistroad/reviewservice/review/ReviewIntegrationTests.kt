package kr.bistroad.reviewservice.review

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import io.mockk.every
import kr.bistroad.reviewservice.review.application.ReviewDto
import kr.bistroad.reviewservice.review.domain.Review
import kr.bistroad.reviewservice.review.infrastructure.ReviewRepository
import kr.bistroad.reviewservice.review.presentation.ReviewRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
internal class ReviewIntegrationTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @MockkBean
    private lateinit var restTemplate: RestTemplate

    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    @Test
    fun `Gets a review`() {
        val review = Review(
            storeId = UUID.randomUUID(),
            itemId = UUID.randomUUID(),
            writerId = UUID.randomUUID(),
            orderId = UUID.randomUUID(),
            contents = "What a nice dish",
            stars = 5
        )

        reviewRepository.save(review)

        every {
            restTemplate.getForObject<ReviewDto.ForResult.Writer>(any<String>())
        } returns ReviewDto.ForResult.Writer(id = review.writerId, username = "john", fullName = "John")

        mockMvc.perform(
            get("/reviews/${review.id}")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(review.id.toString()))
            .andExpect(jsonPath("\$.storeId").value(review.storeId.toString()))
            .andExpect(jsonPath("\$.contents").value(review.contents))
            .andExpect(jsonPath("\$.stars").value(review.stars))
    }

    @Test
    fun `Searches reviews`() {
        val writerId = UUID.randomUUID()
        val reviewA = Review(
            storeId = UUID.randomUUID(),
            itemId = UUID.randomUUID(),
            writerId = writerId,
            orderId = UUID.randomUUID(),
            contents = "Great :)",
            stars = 5
        )
        val reviewB = Review(
            storeId = UUID.randomUUID(),
            itemId = UUID.randomUUID(),
            writerId = writerId,
            orderId = UUID.randomUUID(),
            contents = "Bad :(",
            stars = 1
        )
        val reviewC = Review(
            storeId = UUID.randomUUID(),
            itemId = UUID.randomUUID(),
            writerId = writerId,
            orderId = UUID.randomUUID(),
            contents = "So so",
            stars = 2
        )

        reviewRepository.save(reviewA)
        reviewRepository.save(reviewB)
        reviewRepository.save(reviewC)

        every {
            restTemplate.getForObject<ReviewDto.ForResult.Writer>(any<String>())
        } returns ReviewDto.ForResult.Writer(id = writerId, username = "john", fullName = "John")

        mockMvc.perform(
            get("/reviews?sort=stars,asc")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(reviewB.id.toString()))
            .andExpect(jsonPath("\$.[1].id").value(reviewC.id.toString()))
            .andExpect(jsonPath("\$.[2].id").value(reviewA.id.toString()))
    }

    @Test
    fun `Posts a review`() {
        val body = ReviewRequest.PostBody(
            storeId = UUID.randomUUID(),
            itemId = UUID.randomUUID(),
            writerId = UUID.randomUUID(),
            orderId = UUID.randomUUID(),
            contents = "What a nice dish",
            stars = 5
        )

        every {
            restTemplate.getForObject<ReviewDto.ForResult.Writer>(any<String>())
        } returns ReviewDto.ForResult.Writer(id = body.writerId, username = "john", fullName = "John")

        mockMvc.perform(
            post("/reviews")
                .header("Authorization-Role", "ROLE_ADMIN")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").exists())
            .andExpect(jsonPath("\$.storeId").value(body.storeId.toString()))
            .andExpect(jsonPath("\$.contents").value(body.contents))
            .andExpect(jsonPath("\$.stars").value(body.stars))
    }

    @Test
    fun `Patches a review`() {
        val review = Review(
            storeId = UUID.randomUUID(),
            itemId = UUID.randomUUID(),
            writerId = UUID.randomUUID(),
            orderId = UUID.randomUUID(),
            contents = "What a nice dish",
            stars = 5
        )
        val body = ReviewRequest.PatchBody(
            stars = 4
        )

        every {
            restTemplate.getForObject<ReviewDto.ForResult.Writer>(any<String>())
        } returns ReviewDto.ForResult.Writer(id = review.writerId, username = "john", fullName = "John")

        reviewRepository.save(review)

        mockMvc.perform(
            patch("/reviews/${review.id}")
                .header("Authorization-Role", "ROLE_ADMIN")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").exists())
            .andExpect(jsonPath("\$.storeId").value(review.storeId.toString()))
            .andExpect(jsonPath("\$.contents").value(review.contents))
            .andExpect(jsonPath("\$.stars").value(4))
    }

    @Test
    fun `Deletes a review`() {
        val reviewA = Review(
            storeId = UUID.randomUUID(),
            itemId = UUID.randomUUID(),
            writerId = UUID.randomUUID(),
            orderId = UUID.randomUUID(),
            contents = "Great :)",
            stars = 5
        )
        val reviewB = Review(
            storeId = UUID.randomUUID(),
            itemId = UUID.randomUUID(),
            writerId = UUID.randomUUID(),
            orderId = UUID.randomUUID(),
            contents = "Bad :(",
            stars = 1
        )

        reviewRepository.save(reviewA)
        reviewRepository.save(reviewB)

        mockMvc.perform(
            delete("/reviews/${reviewA.id}")
                .header("Authorization-Role", "ROLE_ADMIN")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)
            .andExpect(content().string(""))

        val reviews = reviewRepository.findAll()
        reviews.shouldBeSingleton()
        reviews.first().shouldBe(reviewB)
    }
}