package nocta.lualvesdecorcatalog.repository.entity

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("themes")
data class ThemeEntity(
    @Id
    val id: UUID? = null,
    val slug: String,
    val name: String,
    val description: String?,
    @Column("age_group")
    val ageGroup: String,
    @Column("price_min")
    val priceMin: BigDecimal?,
    @Column("price_max")
    val priceMax: BigDecimal?,
    val images: String?,
    val active: Boolean,
    @Column("created_at")
    val createdAt: OffsetDateTime,
    @Column("updated_at")
    val updatedAt: OffsetDateTime,
)
