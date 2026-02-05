package nocta.lualvesdecorcatalog.core.service

import nocta.lualvesdecorcatalog.core.item.CreateItemCommand
import nocta.lualvesdecorcatalog.core.item.Item
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.core.item.PatchItemCommand
import nocta.lualvesdecorcatalog.core.item.UpdateItemCommand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ItemService {
    suspend fun create(command: CreateItemCommand): Item
    suspend fun getById(id: UUID): Item
    suspend fun list(name: String?, category: ItemCategory?, active: Boolean?, pageable: Pageable): Page<Item>
    suspend fun update(id: UUID, command: UpdateItemCommand): Item
    suspend fun patch(id: UUID, command: PatchItemCommand): Item
    suspend fun delete(id: UUID)
}
