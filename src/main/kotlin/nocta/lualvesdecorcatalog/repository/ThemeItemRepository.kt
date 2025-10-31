package nocta.lualvesdecorcatalog.repository

import kotlinx.coroutines.flow.Flow
import nocta.lualvesdecorcatalog.repository.entity.ThemeItemEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ThemeItemRepository : CoroutineCrudRepository<ThemeItemEntity, Long> {
    fun findAllByThemeId(themeId: Long): Flow<ThemeItemEntity>
}
