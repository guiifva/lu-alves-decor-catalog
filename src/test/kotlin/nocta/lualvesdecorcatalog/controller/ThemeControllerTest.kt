package nocta.lualvesdecorcatalog.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import nocta.lualvesdecorcatalog.controller.dto.ThemeRequest
import nocta.lualvesdecorcatalog.core.entity.Theme
import nocta.lualvesdecorcatalog.core.service.ThemeService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.math.BigDecimal
import java.util.UUID

@WebFluxTest(ThemeController::class)
class ThemeControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var themeService: ThemeService

    @Test
    fun `should create a theme and return 201`() {
        val themeRequest = ThemeRequest(
            name = "Festa Junina",
            description = "Decoração para festa junina",
            ageGroup = "All",
            priceMin = BigDecimal("100.00"),
            priceMax = BigDecimal("500.00"),
            images = listOf("image1.jpg"),
            active = true
        )

        val theme = Theme(
            id = UUID.randomUUID(),
            name = "Festa Junina",
            description = "Decoração para festa junina",
            ageGroup = "All",
            priceMin = BigDecimal("100.00"),
            priceMax = BigDecimal("500.00"),
            images = listOf("image1.jpg"),
            active = true
        )

        coEvery { themeService.create(any()) } returns theme

        webTestClient.post().uri("/v1/themes")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(themeRequest))
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isNotEmpty
            .jsonPath("$.name").isEqualTo("Festa Junina")
            .jsonPath("$.description").isEqualTo("Decoração para festa junina")
    }
}
