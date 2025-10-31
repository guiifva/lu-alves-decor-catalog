package nocta.lualvesdecorcatalog.core.service

import nocta.lualvesdecorcatalog.core.entity.DecorItem
import java.util.UUID

interface DecorItemService {
    suspend fun getById(id: UUID): DecorItem
}
