package nocta.lualvesdecorcatalog.controller

import jakarta.validation.Valid
import nocta.lualvesdecorcatalog.controller.dto.ThemeRequest
import nocta.lualvesdecorcatalog.controller.dto.ThemeResponse
import nocta.lualvesdecorcatalog.core.service.ThemeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/themes")
class ThemeController(
    private val themeService: ThemeService
) {

    @PostMapping
    suspend fun create(@Valid @RequestBody themeRequest: ThemeRequest): ResponseEntity<ThemeResponse> {
        val theme = themeRequest.toTheme()
        val createdTheme = themeService.create(theme)
        val themeResponse = ThemeResponse.from(createdTheme)
        return ResponseEntity.status(HttpStatus.CREATED).body(themeResponse)
    }
}
