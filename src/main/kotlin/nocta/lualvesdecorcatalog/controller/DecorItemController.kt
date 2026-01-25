package nocta.lualvesdecorcatalog.controller

import nocta.lualvesdecorcatalog.controller.dto.CreateDecorItemRequest
import nocta.lualvesdecorcatalog.controller.dto.DecorItemResponse
import nocta.lualvesdecorcatalog.core.entity.DecorItemCategory
import nocta.lualvesdecorcatalog.core.service.DecorItemService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/items")
class DecorItemController(
    private val decorItemService: DecorItemService
) : DecorItemControllerApi {

    override suspend fun create(request: CreateDecorItemRequest): DecorItemResponse {
        val createdItem = decorItemService.create(request)
        return DecorItemResponse.from(createdItem)
    }

    override suspend fun list(
        name: String?,
        category: DecorItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<DecorItemResponse> {
        return decorItemService.list(name, category, active, pageable)
            .map { DecorItemResponse.from(it) }
    }

    override suspend fun getById(id: UUID): DecorItemResponse {
        val item = decorItemService.getById(id)
        return DecorItemResponse.from(item)
    }
}
