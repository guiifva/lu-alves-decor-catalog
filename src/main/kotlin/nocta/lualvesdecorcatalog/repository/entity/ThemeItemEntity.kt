package nocta.lualvesdecorcatalog.repository.entity

import java.util.UUID
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("theme_items")
data class ThemeItemEntity(
    @Column("theme_id")
    val themeId: UUID,
    @Column("item_id")
    val itemId: UUID
)
