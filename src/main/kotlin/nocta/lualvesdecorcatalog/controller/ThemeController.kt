package nocta.lualvesdecorcatalog.controller

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import java.util.UUID
import nocta.lualvesdecorcatalog.controller.dto.PagedResponse
import nocta.lualvesdecorcatalog.controller.dto.PatchThemeRequest
import nocta.lualvesdecorcatalog.controller.dto.ThemeItemsRequest
import nocta.lualvesdecorcatalog.controller.dto.ThemeItemsResponse
import nocta.lualvesdecorcatalog.controller.dto.ThemeRequest
import nocta.lualvesdecorcatalog.controller.dto.ThemeResponse
import nocta.lualvesdecorcatalog.controller.dto.UpdateThemeRequest
import nocta.lualvesdecorcatalog.controller.dto.item.ItemResponse
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.core.service.ThemeService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
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

@RestController
@RequestMapping("/v1/themes")
@Tag(name = "Themes", description = "Operations related to decoration themes")
class ThemeController(
    private val themeService: ThemeService
) {

    @Operation(summary = "Create theme")
    @PostMapping
    suspend fun create(@Valid @RequestBody request: ThemeRequest): ResponseEntity<ThemeResponse> {
        val created = themeService.create(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED).body(ThemeResponse.from(created))
    }

    @Operation(summary = "Get theme by ID")
    @GetMapping("/{id}")
    suspend fun getById(
        @PathVariable id: UUID,
        @RequestParam(required = false, defaultValue = "false") include: String?
    ): ResponseEntity<ThemeResponse> {
        val includeItems = include == "items"
        val theme = themeService.getById(id, includeItems)
        return ResponseEntity.ok(ThemeResponse.from(theme))
    }

    @Operation(summary = "List themes")
    @GetMapping
    suspend fun list(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false, name = "age_group") ageGroup: String?,
        @RequestParam(required = false) active: Boolean?,
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): ResponseEntity<PagedResponse<ThemeResponse>> {
        val page = themeService.list(name, ageGroup, active, pageable)
            .map { ThemeResponse.from(it) }
        return ResponseEntity.ok(PagedResponse.from(page))
    }

    @Operation(summary = "Update theme")
    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateThemeRequest
    ): ResponseEntity<ThemeResponse> {
        val updated = themeService.update(id, request.toCommand())
        return ResponseEntity.ok(ThemeResponse.from(updated))
    }

    @Operation(summary = "Patch theme")
    @PatchMapping("/{id}")
    suspend fun patch(
        @PathVariable id: UUID,
        @Valid @RequestBody request: PatchThemeRequest
    ): ResponseEntity<ThemeResponse> {
        val updated = themeService.patch(id, request.toCommand())
        return ResponseEntity.ok(ThemeResponse.from(updated))
    }

    @Operation(summary = "Delete theme")
    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        themeService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Add items to theme")
    @PostMapping("/{themeId}/items")
    suspend fun addItems(
        @PathVariable themeId: UUID,
        @RequestBody request: ThemeItemsRequest
    ): ResponseEntity<ThemeItemsResponse> {
        themeService.addItems(themeId, request.itemIds)
        return ResponseEntity.ok(ThemeItemsResponse(request.itemIds))
    }
    
    @Operation(summary = "Remove item from theme")
    @DeleteMapping("/{themeId}/items/{itemId}")
    suspend fun removeItem(
        @PathVariable themeId: UUID,
        @PathVariable itemId: UUID
    ): ResponseEntity<Unit> {
        themeService.removeItem(themeId, itemId)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "List items of a theme")
    @GetMapping("/{themeId}/items")
    suspend fun listItems(
        @PathVariable themeId: UUID,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) category: ItemCategory?,
        @RequestParam(required = false) active: Boolean?,
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): ResponseEntity<PagedResponse<ItemResponse>> {
        val page = themeService.listItems(themeId, name, category, active, pageable)
            .map { ItemResponse.from(it) }
        return ResponseEntity.ok(PagedResponse.from(page))
    }
}