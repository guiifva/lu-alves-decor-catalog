package nocta.lualvesdecorcatalog.core.exception

import java.util.UUID

data class DecorItemNotFoundException(val itemId: UUID) : RuntimeException("Decor item with id $itemId was not found")
