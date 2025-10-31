package nocta.lualvesdecorcatalog.controller.dto

import nocta.lualvesdecorcatalog.core.entity.ThemeItem

data class ThemeItemResponse(
    val name: String,
    val quantity: Int,
    val description: String?
) {
    companion object {
        fun from(item: ThemeItem) = ThemeItemResponse(
            name = item.name,
            quantity = item.quantity,
            description = item.description
        )
    }
}
