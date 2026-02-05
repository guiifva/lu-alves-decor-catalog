package nocta.lualvesdecorcatalog.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import nocta.lualvesdecorcatalog.core.entity.CreateThemeCommand
import nocta.lualvesdecorcatalog.core.entity.PatchThemeCommand
import nocta.lualvesdecorcatalog.core.entity.UpdateThemeCommand

data class ThemeRequest(
    @Schema(description = "Name of the theme", example = "Festa Junina", required = true)
    @field:NotBlank
    @field:Size(min = 2, max = 120)
    val name: String,

    @Schema(description = "Detailed description of the theme", example = "Decoração completa com bandeirinhas e fogueira cenográfica")
    val description: String?,

    @Schema(description = "Target age group for the theme", example = "Infantil", required = true)
    @JsonProperty("age_group")
    @field:NotBlank
    val ageGroup: String,

    @Schema(description = "Minimum estimated price for the theme", example = "150.00")
    @JsonProperty("price_min")
    @field:DecimalMin("0.0")
    val priceMin: BigDecimal?,

    @Schema(description = "Maximum estimated price for the theme", example = "500.00")
    @JsonProperty("price_max")
    @field:DecimalMin("0.0")
    val priceMax: BigDecimal?,

    @Schema(description = "List of image URLs showcasing the theme")
    val images: List<String>?,

    @Schema(description = "Whether the theme is active and available for rent", defaultValue = "true")
    val active: Boolean?
) {
    fun toCommand() = CreateThemeCommand(
        name = name,
        description = description,
        ageGroup = ageGroup,
        priceMin = priceMin,
        priceMax = priceMax,
        images = images,
        active = active
    )
}

data class UpdateThemeRequest(
    @Schema(description = "Name of the theme", example = "Festa Junina Updated", required = true)
    @field:NotBlank
    @field:Size(min = 2, max = 120)
    val name: String,

    @Schema(description = "Detailed description of the theme", example = "Updated description")
    val description: String?,

    @Schema(description = "Target age group for the theme", example = "Infantil", required = true)
    @JsonProperty("age_group")
    @field:NotBlank
    val ageGroup: String,

    @Schema(description = "Minimum estimated price for the theme", example = "200.00")
    @JsonProperty("price_min")
    @field:DecimalMin("0.0")
    val priceMin: BigDecimal?,

    @Schema(description = "Maximum estimated price for the theme", example = "600.00")
    @JsonProperty("price_max")
    @field:DecimalMin("0.0")
    val priceMax: BigDecimal?,

    @Schema(description = "List of image URLs showcasing the theme")
    val images: List<String>?,

    @Schema(description = "Whether the theme is active and available for rent", required = true)
    val active: Boolean
) {
    fun toCommand() = UpdateThemeCommand(
        name = name,
        description = description,
        ageGroup = ageGroup,
        priceMin = priceMin,
        priceMax = priceMax,
        images = images ?: emptyList(),
        active = active
    )
}

data class PatchThemeRequest(
    @Schema(description = "Name of the theme", example = "Festa Junina Patched")
    @field:Size(min = 2, max = 120)
    val name: String?,

    @Schema(description = "Detailed description of the theme")
    val description: String?,

    @Schema(description = "Target age group for the theme")
    @JsonProperty("age_group")
    val ageGroup: String?,

    @Schema(description = "Minimum estimated price for the theme")
    @JsonProperty("price_min")
    @field:DecimalMin("0.0")
    val priceMin: BigDecimal?,

    @Schema(description = "Maximum estimated price for the theme")
    @JsonProperty("price_max")
    @field:DecimalMin("0.0")
    val priceMax: BigDecimal?,

    @Schema(description = "List of image URLs showcasing the theme")
    val images: List<String>?,

    @Schema(description = "Whether the theme is active and available for rent")
    val active: Boolean?
) {
    fun toCommand() = PatchThemeCommand(
        name = name,
        description = description,
        ageGroup = ageGroup,
        priceMin = priceMin,
        priceMax = priceMax,
        images = images,
        active = active
    )
}
