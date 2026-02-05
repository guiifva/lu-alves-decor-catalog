package nocta.lualvesdecorcatalog.core.service

import java.util.UUID
import nocta.lualvesdecorcatalog.core.entity.CreateThemeCommand
import nocta.lualvesdecorcatalog.core.entity.PatchThemeCommand
import nocta.lualvesdecorcatalog.core.entity.Theme
import nocta.lualvesdecorcatalog.core.entity.UpdateThemeCommand
import nocta.lualvesdecorcatalog.core.item.Item
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ThemeService {
    suspend fun create(command: CreateThemeCommand): Theme
    suspend fun getById(id: UUID, includeItems: Boolean = false): Theme
    suspend fun list(name: String?, ageGroup: String?, active: Boolean?, pageable: Pageable): Page<Theme>
    suspend fun update(id: UUID, command: UpdateThemeCommand): Theme
    suspend fun patch(id: UUID, command: PatchThemeCommand): Theme
    suspend fun delete(id: UUID)

    suspend fun addItems(themeId: UUID, itemIds: List<UUID>)
    suspend fun removeItem(themeId: UUID, itemId: UUID)
    suspend fun listItems(
        themeId: UUID,
        name: String?,
        category: ItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<Item>
}
