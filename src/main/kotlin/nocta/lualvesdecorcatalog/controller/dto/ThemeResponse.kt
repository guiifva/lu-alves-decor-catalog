package nocta.lualvesdecorcatalog.controller.dto

import nocta.lualvesdecorcatalog.core.entity.Theme

data class ThemeResponse(
    val id: Long?,
    val name: String,
    val description: String?,
    val items: List<ThemeItemResponse>
) {
    companion object {
        fun from(theme: Theme) = ThemeResponse(
            id = theme.id,
            name = theme.name,
            description = theme.description,
            items = theme.items.map { ThemeItemResponse.from(it) }
        )
    }
}
