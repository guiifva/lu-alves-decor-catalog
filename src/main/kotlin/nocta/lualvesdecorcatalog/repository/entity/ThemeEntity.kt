package nocta.lualvesdecorcatalog.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("themes")
data class ThemeEntity(
    @Id
    val id: Long? = null,
    val name: String,
    val description: String?
)
