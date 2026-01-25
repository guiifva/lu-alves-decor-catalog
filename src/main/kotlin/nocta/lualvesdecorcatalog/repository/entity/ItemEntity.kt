package nocta.lualvesdecorcatalog.repository.entity

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("decor_items")
data class ItemEntity(
    @Id
    val id: UUID?,
    val sku: String?,
    val name: String,
    val category: String,
    val description: String?,
    @Column("quantity_available")
    val quantityAvailable: Int,
    val unit: String?,
    @Column("replacement_value")
    val replacementValue: BigDecimal?,
    @Column("rental_price")
    val rentalPrice: BigDecimal?,
    val photos: String?,
    val active: Boolean,
    @Column("created_at")
    val createdAt: OffsetDateTime,
    @Column("updated_at")
    val updatedAt: OffsetDateTime,
)
