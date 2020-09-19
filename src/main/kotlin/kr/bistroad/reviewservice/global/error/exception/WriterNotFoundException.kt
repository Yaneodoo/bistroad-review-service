package kr.bistroad.reviewservice.global.error.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Writer not found")
class WriterNotFoundException : RuntimeException {
    constructor(throwable: Throwable): super(throwable)
    constructor(): super()
}