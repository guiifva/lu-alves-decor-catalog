package nocta.lualvesdecorcatalog.repository

import java.util.UUID
import kotlinx.coroutines.flow.Flow
import nocta.lualvesdecorcatalog.repository.entity.ThemeItemEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.Repository

interface ThemeItemRepository : Repository<ThemeItemEntity, UUID> {
    
    @Modifying
    @Query("DELETE FROM theme_items WHERE theme_id = :themeId AND item_id = :itemId")
    suspend fun deleteByThemeIdAndItemId(themeId: UUID, itemId: UUID)

    @Modifying
    @Query("INSERT INTO theme_items (theme_id, item_id) VALUES (:themeId, :itemId) ON CONFLICT DO NOTHING")
    suspend fun addLink(themeId: UUID, itemId: UUID): Int
    
    @Query("SELECT item_id FROM theme_items WHERE theme_id = :themeId")
    fun findItemIdsByThemeId(themeId: UUID): Flow<UUID>

    @Modifying
    @Query("DELETE FROM theme_items WHERE theme_id = :themeId")
    suspend fun deleteAllByThemeId(themeId: UUID)
}
