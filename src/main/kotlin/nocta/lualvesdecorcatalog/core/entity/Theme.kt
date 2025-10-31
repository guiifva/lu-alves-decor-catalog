package nocta.lualvesdecorcatalog.core.entity

data class Theme(
    val id: Long? = null,
    val name: String,
    val description: String?,
    val items: List<ThemeItem>
)
