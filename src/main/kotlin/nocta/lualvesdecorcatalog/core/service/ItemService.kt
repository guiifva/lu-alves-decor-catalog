package nocta.lualvesdecorcatalog.core.service

import nocta.lualvesdecorcatalog.core.item.CreateItemCommand
import nocta.lualvesdecorcatalog.core.item.Item

interface ItemService {
    suspend fun create(command: CreateItemCommand): Item
}
