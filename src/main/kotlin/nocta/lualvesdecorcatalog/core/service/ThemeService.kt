package nocta.lualvesdecorcatalog.core.service

import nocta.lualvesdecorcatalog.core.entity.Theme

interface ThemeService {
    suspend fun create(theme: Theme): Theme
}
