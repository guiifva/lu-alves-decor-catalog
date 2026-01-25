package nocta.lualvesdecorcatalog.controller.dto.item

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import nocta.lualvesdecorcatalog.core.item.CreateItemCommand
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.core.item.ItemUnit

private const val SKU_PATTERN = "^[A-Z0-9_-]{3,32}$"

data class CreateItemRequest(
    @field:Pattern(
        regexp = SKU_PATTERN,
        message = "must match pattern $SKU_PATTERN",
    )
    val sku: String? = null,
    @field:NotBlank
    @field:Size(min = 2, max = 120)
    val name: String,
    @field:NotNull
    val category: ItemCategory,
    val description: String? = null,
    @JsonProperty("quantity_available")
    @field:Min(0)
    val quantityAvailable: Int,
    val unit: ItemUnit? = null,
    @JsonProperty("replacement_value")
    @field:DecimalMin(value = "0.0", inclusive = true)
    val replacementValue: BigDecimal? = null,
    @JsonProperty("rental_price")
    @field:DecimalMin(value = "0.0", inclusive = true)
    val rentalPrice: BigDecimal? = null,
    val photos: List<String>? = null,
    val active: Boolean? = null,
) {
    fun toCommand() = CreateItemCommand(
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
    )
}
