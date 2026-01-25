package nocta.lualvesdecorcatalog.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import nocta.lualvesdecorcatalog.controller.dto.item.CreateItemRequest
import nocta.lualvesdecorcatalog.core.exception.DomainValidationException
import nocta.lualvesdecorcatalog.core.exception.DuplicateResourceException
import nocta.lualvesdecorcatalog.core.item.Item
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.core.item.ItemUnit
import nocta.lualvesdecorcatalog.core.service.ItemService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@WebFluxTest(ItemController::class)
@Import(RestExceptionHandler::class)
class ItemControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var itemService: ItemService

    @Test
    fun `should create an item and return 201`() {
        val request = CreateItemRequest(
            name = "Arco de Balões Pastel",
            category = ItemCategory.Balloon_Arch,
            quantityAvailable = 3,
            photos = listOf("https://cdn.example.com/items/balloon-arch-pastel.jpg"),
        )

        val now = OffsetDateTime.parse("2025-10-30T22:00:00Z")
        val item = Item(
            id = UUID.fromString("b0a6f8f8-3e91-4a15-9f69-9f2c7f86b8d1"),
            sku = null,
            name = request.name,
            category = request.category,
            description = null,
            quantityAvailable = request.quantityAvailable,
            unit = null,
            replacementValue = null,
            rentalPrice = null,
            photos = request.photos!!,
            active = true,
            createdAt = now,
            updatedAt = now,
        )

        coEvery { itemService.create(request.toCommand()) } returns item

        webTestClient.post().uri("/v1/items")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isEqualTo("b0a6f8f8-3e91-4a15-9f69-9f2c7f86b8d1")
            .jsonPath("$.name").isEqualTo(request.name)
            .jsonPath("$.category").isEqualTo(request.category.name)
            .jsonPath("$.quantity_available").isEqualTo(request.quantityAvailable)
            .jsonPath("$.photos[0]").isEqualTo(request.photos!![0])
            .jsonPath("$.active").isEqualTo(true)
    }

    @Test
    fun `should return 422 when domain validation fails`() {
        val request = CreateItemRequest(
            name = "Panel",
            category = ItemCategory.Panel,
            quantityAvailable = 1,
        )

        coEvery { itemService.create(request.toCommand()) } throws DomainValidationException(
            listOf("Item name must have between 2 and 120 characters.")
        )

        webTestClient.post().uri("/v1/items")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody()
            .jsonPath("$.title").isEqualTo("Unprocessable Entity")
            .jsonPath("$.violations[0]").isEqualTo("Item name must have between 2 and 120 characters.")
    }

    @Test
    fun `should return 409 when sku already exists`() {
        val request = CreateItemRequest(
            sku = "SKU123",
            name = "Arco de Balões Pastel",
            category = ItemCategory.Balloon_Arch,
            quantityAvailable = 1,
            photos = listOf("https://cdn.example.com/items/balloon-arch-pastel.jpg"),
            unit = ItemUnit.UNIT,
            replacementValue = BigDecimal("10.00"),
            rentalPrice = BigDecimal("5.00"),
        )

        coEvery { itemService.create(request.toCommand()) } throws DuplicateResourceException(
            "Item with sku 'SKU123' already exists."
        )

        webTestClient.post().uri("/v1/items")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody()
            .jsonPath("$.title").isEqualTo("Conflict")
            .jsonPath("$.detail").isEqualTo("Item with sku 'SKU123' already exists.")
    }
}
