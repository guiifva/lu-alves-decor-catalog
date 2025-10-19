package nocta.lualvesdecorcatalog.controller.dto

import nocta.lualvesdecorcatalog.core.entity.Item

data class ItemResponse(
    val name: String,
    val quantity: Int,
    val description: String?
) {
    companion object {
        fun from(item: Item) = ItemResponse(
            name = item.name,
            quantity = item.quantity,
            description = item.description
        )
    }
}
