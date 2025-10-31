package nocta.lualvesdecorcatalog.repository

import java.util.UUID
import nocta.lualvesdecorcatalog.repository.entity.ItemEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ItemRepository : CoroutineCrudRepository<ItemEntity, UUID> {
    suspend fun existsBySku(sku: String): Boolean
}
