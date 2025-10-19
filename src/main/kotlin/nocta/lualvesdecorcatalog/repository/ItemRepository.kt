package nocta.lualvesdecorcatalog.repository

import kotlinx.coroutines.flow.Flow
import nocta.lualvesdecorcatalog.repository.entity.ItemEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ItemRepository : CoroutineCrudRepository<ItemEntity, Long> {
    fun findAllByThemeId(themeId: Long): Flow<ItemEntity>
}
