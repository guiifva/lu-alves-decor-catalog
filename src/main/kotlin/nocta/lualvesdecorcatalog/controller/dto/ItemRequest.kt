package nocta.lualvesdecorcatalog.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import nocta.lualvesdecorcatalog.core.entity.Item

data class ItemRequest(
    @field:NotBlank
    val name: String,
    @field:NotNull
    val quantity: Int,
    val description: String?
) {
    fun toItem() = Item(
        name = name,
        quantity = quantity,
        description = description
    )
}
