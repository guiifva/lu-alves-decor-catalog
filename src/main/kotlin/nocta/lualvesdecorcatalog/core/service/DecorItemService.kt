package nocta.lualvesdecorcatalog.core.service

import nocta.lualvesdecorcatalog.controller.dto.CreateDecorItemRequest
import nocta.lualvesdecorcatalog.core.entity.DecorItem
import nocta.lualvesdecorcatalog.core.entity.DecorItemCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface DecorItemService {
    suspend fun create(request: CreateDecorItemRequest): DecorItem
    suspend fun getById(id: UUID): DecorItem
    suspend fun list(name: String?, category: DecorItemCategory?, active: Boolean?, pageable: Pageable): Page<DecorItem>
}
