package nocta.lualvesdecorcatalog.core.entity

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class DecorItem(
    val id: UUID,
    val sku: String?,
    val name: String,
    val category: DecorItemCategory,
    val description: String?,
    val quantityAvailable: Int,
    val unit: DecorItemUnit?,
    val replacementValue: BigDecimal?,
    val rentalPrice: BigDecimal?,
    val photos: List<String>,
    val active: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
