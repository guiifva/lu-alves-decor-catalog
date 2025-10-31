package nocta.lualvesdecorcatalog.controller

import jakarta.validation.Valid
import nocta.lualvesdecorcatalog.controller.dto.item.CreateItemRequest
import nocta.lualvesdecorcatalog.controller.dto.item.ItemResponse
import nocta.lualvesdecorcatalog.core.service.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/items")
class ItemController(
    private val itemService: ItemService,
) {
    @PostMapping
    suspend fun create(@Valid @RequestBody request: CreateItemRequest): ResponseEntity<ItemResponse> {
        val createdItem = itemService.create(request.toCommand())
        val response = ItemResponse.from(createdItem)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}
