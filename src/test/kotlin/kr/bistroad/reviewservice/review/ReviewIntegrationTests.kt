package kr.bistroad.reviewservice.review

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import io.mockk.every
import kr.bistroad.reviewservice.review.domain.*
import kr.bistroad.reviewservice.review.infrastructure.ReviewRepository
import kr.bistroad.reviewservice.review.presentation.ReviewRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
internal class ReviewIntegrationTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @MockkBean
    private lateinit var restTemplate: RestTemplate

    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    @AfterEach
    fun clear() = reviewRepository.deleteAll()

    @Test
    fun `Gets a review`() {
        val review = Review(
            store = Store(UUID.randomUUID()),
            item = ReviewedItem(UUID.randomUUID()),
            writer = Writer(UUID.randomUUID()),
            order = Order(UUID.randomUUID()),
            contents = "What a nice dish",
            stars = 5,
            photo = Photo(
                sourceUrl = "https://httpbin.org",
                thumbnailUrl = "https://httpbin.org"
            )
        )
        reviewRepository.save(review)

        every {
            restTemplate.getForObject<StoreItem>(any<String>())
        } returns StoreItem(review.item.id, "Example", "An example item", 100.0)
        every {
            restTemplate.getForObject<User>(any<String>())
        } returns User(id = review.writer.id, username = "john", fullName = "John")

        mockMvc.perform(
            get("/reviews/${review.id}")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(review.id.toString()))
            .andExpect(jsonPath("\$.storeId").value(review.store.id.toString()))
            .andExpect(jsonPath("\$.contents").value(review.contents))
            .andExpect(jsonPath("\$.stars").value(review.stars))
            .andExpect(jsonPath("\$.photo.sourceUrl").value(review.photo!!.sourceUrl))
    }

    @Test
    fun `Searches reviews`() {
        val writerId = UUID.randomUUID()
        val store = Store(UUID.randomUUID())
        val item = ReviewedItem(UUID.randomUUID())
        val reviewA = Review(
            store = store,
            item = item,
            writer = Writer(writerId),
            order = Order(UUID.randomUUID()),
            contents = "Great :)",
            stars = 5
        )
        val reviewB = Review(
            store = store,
            item = item,
            writer = Writer(writerId),
            order = Order(UUID.randomUUID()),
            contents = "Bad :(",
            stars = 1
        )
        val reviewC = Review(
            store = store,
            item = item,
            writer = Writer(writerId),
            order = Order(UUID.randomUUID()),
            contents = "So so",
            stars = 2
        )

        reviewRepository.save(reviewA)
        reviewRepository.save(reviewB)
        reviewRepository.save(reviewC)

        every {
            restTemplate.getForObject<StoreItem>(any<String>())
        } returns StoreItem(item.id, "Example", "An example item", 100.0)
        every {
            restTemplate.getForObject<User>(any<String>())
        } returns User(id = writerId, username = "john", fullName = "John")

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
            stars = 5,
            timestamp = "2020-09-22T11:31:19Z"
        )

        every {
            restTemplate.getForObject<StoreItem>(any<String>())
        } returns StoreItem(UUID.randomUUID(), "Example", "An example item", 100.0)
        every {
            restTemplate.getForObject<User>(any<String>())
        } returns User(id = body.writerId, username = "john", fullName = "John")

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
            .andExpect(jsonPath("\$.timestamp").value(body.timestamp!!))
    }

    @Test
    fun `Patches a review`() {
        val review = Review(
            store = Store(UUID.randomUUID()),
            item = ReviewedItem(UUID.randomUUID()),
            writer = Writer(UUID.randomUUID()),
            order = Order(UUID.randomUUID()),
            contents = "What a nice dish",
            stars = 5
        )
        val body = ReviewRequest.PatchBody(
            stars = 4
        )

        every {
            restTemplate.getForObject<StoreItem>(any<String>())
        } returns StoreItem(review.item.id, "Example", "An example item", 100.0)
        every {
            restTemplate.getForObject<User>(any<String>())
        } returns User(id = review.writer.id, username = "john", fullName = "John")

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
            .andExpect(jsonPath("\$.storeId").value(review.store.id.toString()))
            .andExpect(jsonPath("\$.contents").value(review.contents))
            .andExpect(jsonPath("\$.stars").value(4))
    }

    @Test
    fun `Deletes a review`() {
        val reviewA = Review(
            store = Store(UUID.randomUUID()),
            item = ReviewedItem(UUID.randomUUID()),
            writer = Writer(UUID.randomUUID()),
            order = Order(UUID.randomUUID()),
            contents = "Great :)",
            stars = 5
        )
        val reviewB = Review(
            store = Store(UUID.randomUUID()),
            item = ReviewedItem(UUID.randomUUID()),
            writer = Writer(UUID.randomUUID()),
            order = Order(UUID.randomUUID()),
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