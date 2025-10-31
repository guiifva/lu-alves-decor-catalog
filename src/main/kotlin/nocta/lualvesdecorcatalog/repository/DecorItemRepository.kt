package nocta.lualvesdecorcatalog.repository

import nocta.lualvesdecorcatalog.repository.entity.DecorItemEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface DecorItemRepository : CoroutineCrudRepository<DecorItemEntity, UUID>
