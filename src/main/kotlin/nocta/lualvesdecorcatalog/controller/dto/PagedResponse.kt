package nocta.lualvesdecorcatalog.controller.dto

import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort

data class PagedResponse<T>(
    val content: List<T>,
    val page: PageMetadata,
    val sort: List<SortResponse>
) {
    companion object {
        fun <T> from(page: Page<T>): PagedResponse<T> {
            return PagedResponse(
                content = page.content,
                page = PageMetadata(
                    number = page.number,
                    size = page.size,
                    totalElements = page.totalElements,
                    totalPages = page.totalPages
                ),
                sort = page.sort.map { SortResponse(it.property, it.direction.name) }.toList()
            )
        }
    }
}

data class PageMetadata(
    val number: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)

data class SortResponse(
    val property: String,
    val direction: String
)
