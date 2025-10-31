package nocta.lualvesdecorcatalog.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import nocta.lualvesdecorcatalog.controller.dto.ThemeItemRequest
import nocta.lualvesdecorcatalog.controller.dto.ThemeRequest
import nocta.lualvesdecorcatalog.core.entity.ThemeItem
import nocta.lualvesdecorcatalog.core.entity.Theme
import nocta.lualvesdecorcatalog.core.service.ThemeService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

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
            items = listOf(
                ThemeItemRequest(
                    name = "Bandeirinhas",
                    quantity = 100,
                    description = "Bandeirinhas coloridas"
                )
            )
        )

        val theme = Theme(
            id = 1L,
            name = "Festa Junina",
            description = "Decoração para festa junina",
            items = listOf(
                ThemeItem(
                    name = "Bandeirinhas",
                    quantity = 100,
                    description = "Bandeirinhas coloridas"
                )
            )
        )

        coEvery { themeService.create(any()) } returns theme

        webTestClient.post().uri("/themes")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(themeRequest))
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.name").isEqualTo("Festa Junina")
            .jsonPath("$.description").isEqualTo("Decoração para festa junina")
            .jsonPath("$.items[0].name").isEqualTo("Bandeirinhas")
    }
}
