package kr.bistroad.reviewservice.review.presentation

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.bistroad.reviewservice.review.application.ReviewPhotoService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@Api(tags = ["/reviews/*/photo"])
class ReviewPhotoController(
    private val reviewPhotoService: ReviewPhotoService
) {
    @PostMapping("/reviews/{id}/photo", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ApiOperation("\${swagger.doc.operation.review.post-review-photo.description}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun postPhoto(@PathVariable id: UUID, @RequestPart file: MultipartFile) {
        reviewPhotoService.upload(id, file)
    }
}