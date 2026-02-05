package nocta.lualvesdecorcatalog.core.exception

import java.util.UUID

class ThemeNotFoundException(id: UUID) : RuntimeException("Theme with id $id not found")
