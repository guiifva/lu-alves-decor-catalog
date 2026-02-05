package nocta.lualvesdecorcatalog.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import nocta.lualvesdecorcatalog.controller.dto.PagedResponse
import nocta.lualvesdecorcatalog.controller.dto.item.CreateItemRequest
import nocta.lualvesdecorcatalog.controller.dto.item.ItemResponse
import nocta.lualvesdecorcatalog.controller.dto.item.PatchItemRequest
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.core.item.UpdateItemCommand
import nocta.lualvesdecorcatalog.core.service.ItemService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/items")
@Tag(name = "Items", description = "Operations related to decoration items")
class ItemController(
    private val itemService: ItemService,
) {
    @Operation(summary = "Create a new item", description = "Creates a new decoration item with the provided details.")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "201",
            description = "Item created successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ItemResponse::class))]
        ),
        ApiResponse(
            responseCode = "422",
            description = "Invalid input (validation error)",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        ),
        ApiResponse(
            responseCode = "409",
            description = "SKU already exists",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        )
    ])
    @PostMapping
    suspend fun create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the item to create", required = true)
        @Valid @RequestBody request: CreateItemRequest
    ): ResponseEntity<ItemResponse> {
        val createdItem = itemService.create(request.toCommand())
        val response = ItemResponse.from(createdItem)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @Operation(summary = "Get item by ID", description = "Retrieves the details of a specific decoration item by its unique identifier.")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Item found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ItemResponse::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Item not found",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        )
    ])
    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): ResponseEntity<ItemResponse> {
        val item = itemService.getById(id)
        return ResponseEntity.ok(ItemResponse.from(item))
    }

    @Operation(summary = "List items", description = "Retrieves a paginated list of decoration items with optional filters.")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "List of items retrieved successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PagedResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid pagination or filter parameters",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        )
    ])
    @GetMapping
    suspend fun list(
        @Parameter(description = "Filter by name (contains, case-insensitive)")
        @RequestParam(required = false) name: String?,

        @Parameter(description = "Filter by category")
        @RequestParam(required = false) category: ItemCategory?,

        @Parameter(description = "Filter by active status")
        @RequestParam(required = false) active: Boolean?,

        @Parameter(description = "Pagination information")
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): ResponseEntity<PagedResponse<ItemResponse>> {
        val page = itemService.list(name, category, active, pageable)
            .map { ItemResponse.from(it) }
        return ResponseEntity.ok(PagedResponse.from(page))
    }

    @Operation(summary = "Update item", description = "Fully updates an existing decoration item.")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Item updated",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ItemResponse::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Item not found",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        ),
        ApiResponse(
            responseCode = "409",
            description = "SKU conflict",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        ),
        ApiResponse(
            responseCode = "422",
            description = "Validation error",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        )
    ])
    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody request: CreateItemRequest
    ): ResponseEntity<ItemResponse> {
        val command = UpdateItemCommand(
            sku = request.sku,
            name = request.name,
            category = request.category,
            description = request.description,
            quantityAvailable = request.quantityAvailable,
            unit = request.unit,
            replacementValue = request.replacementValue,
            rentalPrice = request.rentalPrice,
            photos = request.photos ?: emptyList(),
            active = request.active ?: true
        )
        val updated = itemService.update(id, command)
        return ResponseEntity.ok(ItemResponse.from(updated))
    }

    @Operation(summary = "Partially update item", description = "Partially updates an existing decoration item.")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Item updated",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ItemResponse::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Item not found",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        ),
        ApiResponse(
            responseCode = "409",
            description = "SKU conflict",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        ),
        ApiResponse(
            responseCode = "422",
            description = "Validation error",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        )
    ])
    @PatchMapping("/{id}")
    suspend fun patch(
        @PathVariable id: UUID,
        @Valid @RequestBody request: PatchItemRequest
    ): ResponseEntity<ItemResponse> {
        val updated = itemService.patch(id, request.toCommand())
        return ResponseEntity.ok(ItemResponse.from(updated))
    }

    @Operation(summary = "Delete item", description = "Deletes a decoration item.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Item deleted"),
        ApiResponse(
            responseCode = "404",
            description = "Item not found",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        )
    ])
    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        itemService.delete(id)
        return ResponseEntity.noContent().build()
    }
}