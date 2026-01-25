package nocta.lualvesdecorcatalog.core.exception

class SkuAlreadyExistsException(val sku: String) : RuntimeException("Decor item with SKU $sku already exists.")
