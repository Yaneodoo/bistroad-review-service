package kr.bistroad.reviewservice.review.infrastructure

import kr.bistroad.reviewservice.review.domain.StoreItem
import kr.bistroad.reviewservice.review.domain.StoreItemRepository
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.util.*

@Component
class RestStoreItemRepository(
    private val restTemplate: RestTemplate
) : StoreItemRepository {
    override fun findById(storeId: UUID, itemId: UUID) =
        try {
            restTemplate.getForObject<StoreItem>(
                "http://store-service:8080/stores/$storeId/items/$itemId"
            )
        } catch (ex: HttpClientErrorException.NotFound) {
            null
        }
}