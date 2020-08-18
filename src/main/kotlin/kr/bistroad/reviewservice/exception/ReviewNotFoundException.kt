package kr.bistroad.reviewservice.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Review not found")
class ReviewNotFoundException : RuntimeException()