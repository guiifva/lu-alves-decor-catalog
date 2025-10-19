package nocta.lualvesdecorcatalog.controller.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import nocta.lualvesdecorcatalog.core.entity.Theme

data class ThemeRequest(
    @field:NotBlank
    val name: String,
    val description: String?,
    @field:Valid
    val items: List<ItemRequest>
) {
    fun toTheme() = Theme(
        name = name,
        description = description,
        items = items.map { it.toItem() }
    )
}
