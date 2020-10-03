package kr.bistroad.reviewservice.global.error.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Reviewed item not found")
class ReviewedItemNotFoundException : RuntimeException()