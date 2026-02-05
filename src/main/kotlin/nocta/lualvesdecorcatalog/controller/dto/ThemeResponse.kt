package nocta.lualvesdecorcatalog.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import nocta.lualvesdecorcatalog.controller.dto.item.ItemResponse
import nocta.lualvesdecorcatalog.core.entity.Theme

data class ThemeResponse(
    val id: UUID?,
    val slug: String?,
    val name: String,
    val description: String?,
    @JsonProperty("age_group")
    val ageGroup: String,
    @JsonProperty("price_min")
    val priceMin: BigDecimal?,
    @JsonProperty("price_max")
    val priceMax: BigDecimal?,
    val images: List<String>,
    val active: Boolean,
    @JsonProperty("created_at")
    val createdAt: OffsetDateTime?,
    @JsonProperty("updated_at")
    val updatedAt: OffsetDateTime?,
    val items: List<ItemResponse>?
) {
    companion object {
        fun from(theme: Theme) = ThemeResponse(
            id = theme.id,
            slug = theme.slug,
            name = theme.name,
            description = theme.description,
            ageGroup = theme.ageGroup,
            priceMin = theme.priceMin,
            priceMax = theme.priceMax,
            images = theme.images,
            active = theme.active,
            createdAt = theme.createdAt,
            updatedAt = theme.updatedAt,
            items = theme.items?.map { ItemResponse.from(it) }
        )
    }
}
