package nocta.lualvesdecorcatalog.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import nocta.lualvesdecorcatalog.controller.dto.CreateDecorItemRequest
import nocta.lualvesdecorcatalog.controller.dto.DecorItemResponse
import nocta.lualvesdecorcatalog.core.entity.DecorItemCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

@Tag(name = "Decor Items", description = "Operations related to decoration items")
interface DecorItemControllerApi {

    @Operation(summary = "Create a new decor item", description = "Creates a new decoration item with the provided details.")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "201",
            description = "Item created successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = DecorItemResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        ),
        ApiResponse(
            responseCode = "409",
            description = "SKU already exists",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        )
    ])
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(
        @RequestBody(description = "Details of the item to create", required = true)
        @Valid request: CreateDecorItemRequest
    ): DecorItemResponse

    @Operation(summary = "List decor items", description = "Retrieves a paginated list of decoration items with optional filters.")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "List of items retrieved successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Page::class))]
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
        @RequestParam(required = false) category: DecorItemCategory?,

        @Parameter(description = "Filter by active status")
        @RequestParam(required = false) active: Boolean?,

        @Parameter(description = "Pagination information")
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): Page<DecorItemResponse>

    @Operation(summary = "Get item by ID", description = "Retrieves the details of a specific decoration item by its unique identifier.")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Item found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = DecorItemResponse::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Item not found",
            content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]
        )
    ])
    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): DecorItemResponse
}