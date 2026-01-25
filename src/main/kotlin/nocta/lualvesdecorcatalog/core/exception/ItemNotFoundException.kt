package nocta.lualvesdecorcatalog.core.exception

import java.util.UUID

data class ItemNotFoundException(val itemId: UUID) : RuntimeException("Item with id $itemId was not found")
