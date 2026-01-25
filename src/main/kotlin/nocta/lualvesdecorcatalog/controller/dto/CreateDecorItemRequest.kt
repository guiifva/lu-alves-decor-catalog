package nocta.lualvesdecorcatalog.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import nocta.lualvesdecorcatalog.core.entity.DecorItem
import nocta.lualvesdecorcatalog.core.entity.DecorItemCategory
import nocta.lualvesdecorcatalog.core.entity.DecorItemUnit
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateDecorItemRequest(
    @field:NotBlank
    @field:Size(min = 2, max = 120)
    val name: String,

    @field:NotNull
    val category: DecorItemCategory,

    @field:NotNull
    @field:Min(0)
    val quantityAvailable: Int,

    @field:Pattern(regexp = "^[A-Z0-9_-]{3,32}$")
    val sku: String? = null,

    val description: String? = null,

    val unit: DecorItemUnit? = null,

    @field:Min(0)
    val replacementValue: BigDecimal? = null,

    @field:Min(0)
    val rentalPrice: BigDecimal? = null,

    val photos: List<String> = emptyList(),

    val active: Boolean = true
) {
    fun toDomain(): DecorItem {
        val now = OffsetDateTime.now()
        return DecorItem(
            id = UUID.randomUUID(),
            sku = sku,
            name = name,
            category = category,
            description = description,
            quantityAvailable = quantityAvailable,
            unit = unit,
            replacementValue = replacementValue,
            rentalPrice = rentalPrice,
            photos = photos,
            active = active,
            createdAt = now,
            updatedAt = now
        )
    }
}
