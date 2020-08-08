package kr.bistroad.reviewservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.ribbon.RibbonClient

@SpringBootApplication
@EnableDiscoveryClient
@RibbonClient(name = "review-service")
class BistroadReviewServiceApplication

fun main(args: Array<String>) {
    runApplication<BistroadReviewServiceApplication>(*args)
}
