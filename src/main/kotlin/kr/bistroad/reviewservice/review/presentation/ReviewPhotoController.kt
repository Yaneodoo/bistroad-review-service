package kr.bistroad.reviewservice.review.presentation

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.bistroad.reviewservice.review.application.ReviewPhotoService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@Api(tags = ["/reviews/*/photo"])
class ReviewPhotoController(
    private val reviewPhotoService: ReviewPhotoService
) {
    @PostMapping("/reviews/{id}/photo", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ApiOperation("\${swagger.doc.operation.review.post-review-photo.description}")
    fun postPhoto(@PathVariable id: UUID, @RequestPart file: MultipartFile) =
        reviewPhotoService.upload(id, file)
}