package nocta.lualvesdecorcatalog.repository

import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.repository.entity.ItemEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ItemRepositoryCustom {
    suspend fun findByFilters(
        name: String?,
        category: ItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<ItemEntity>

    suspend fun findByThemeIdAndFilters(
        themeId: UUID,
        name: String?,
        category: ItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<ItemEntity>
}