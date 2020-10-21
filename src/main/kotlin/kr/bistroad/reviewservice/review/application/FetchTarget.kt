package kr.bistroad.reviewservice.review.application

enum class FetchTarget(
    private val value: String
) {
    STORE_ITEM("store-item");

    companion object {
        fun from(value: String) = values().first { it.value == value }
    }
}