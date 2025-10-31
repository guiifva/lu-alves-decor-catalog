package nocta.lualvesdecorcatalog.controller

import nocta.lualvesdecorcatalog.controller.dto.DecorItemResponse
import nocta.lualvesdecorcatalog.core.service.DecorItemService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/items")
class DecorItemController(
    private val decorItemService: DecorItemService
) {
    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): DecorItemResponse {
        val item = decorItemService.getById(id)
        return DecorItemResponse.from(item)
    }
}
