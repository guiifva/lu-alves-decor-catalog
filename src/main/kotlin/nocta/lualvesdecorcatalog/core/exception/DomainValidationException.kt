package nocta.lualvesdecorcatalog.core.exception

class DomainValidationException(
    val violations: List<String>,
    cause: Throwable? = null,
) : RuntimeException(violations.joinToString(separator = "; "), cause)
