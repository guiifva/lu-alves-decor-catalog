package nocta.lualvesdecorcatalog.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("items")
data class ItemEntity(
    @Id
    val id: Long? = null,
    val name: String,
    val quantity: Int,
    val description: String?,
    val themeId: Long
)
