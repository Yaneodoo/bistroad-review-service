package kr.bistroad.reviewservice.review

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "reviews")
class Review(
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        val id: UUID? = null,

        @Column(columnDefinition = "BINARY(16)")
        val storeId: UUID,

        @Column(columnDefinition = "BINARY(16)")
        val itemId: UUID,

        @Column(columnDefinition = "BINARY(16)")
        var writerId: UUID,

        @Column(columnDefinition = "BINARY(16)")
        var orderId: UUID,

        var contents: String,
        var stars: Int
)