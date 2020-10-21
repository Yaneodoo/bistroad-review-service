package kr.bistroad.reviewservice.review.infrastructure

import kr.bistroad.reviewservice.review.domain.User
import kr.bistroad.reviewservice.review.domain.UserRepository
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.util.*

@Component
class RestUserRepository(
    private val restTemplate: RestTemplate
) : UserRepository {
    override fun findById(id: UUID) =
        try {
            restTemplate.getForObject<User>(
                "http://user-service:8080/users/$id"
            )
        } catch (ex: HttpClientErrorException.NotFound) {
            null
        }
}