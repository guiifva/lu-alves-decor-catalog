package nocta.lualvesdecorcatalog.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("theme_items")
data class ThemeItemEntity(
    @Id
    val id: Long? = null,
    val name: String,
    val quantity: Int,
    val description: String?,
    val themeId: Long
)
