package nocta.lualvesdecorcatalog.controller

import kotlinx.coroutines.runBlocking
import nocta.lualvesdecorcatalog.core.item.Item
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.core.item.ItemUnit
import nocta.lualvesdecorcatalog.repository.ItemRepository
import nocta.lualvesdecorcatalog.repository.mapper.toEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@org.springframework.test.context.ActiveProfiles("test")
class ItemControllerIT @Autowired constructor(
    private val webTestClient: WebTestClient,
    private val itemRepository: ItemRepository
) {

    @BeforeEach
    fun setUp() = runBlocking {
        itemRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() = runBlocking {
        itemRepository.deleteAll()
    }

    @Test
    fun `should return item when it exists`() = runBlocking {
        val id = UUID.randomUUID()
        val now = OffsetDateTime.parse("2024-01-01T10:00:00Z")
        val item = Item(
            id = id,
            sku = "SKU-123",
            name = "Arco de Balões",
            category = ItemCategory.Balloon_Arch,
            description = "Arco completo em tons pastel",
            quantityAvailable = 3,
            unit = ItemUnit.SET,
            replacementValue = BigDecimal("1500.00"),
            rentalPrice = BigDecimal("450.00"),
            photos = listOf("https://cdn.example.com/balloon-arch.jpg"),
            active = true,
            createdAt = now,
            updatedAt = now
        )
        itemRepository.save(item.toEntity())

        webTestClient.get()
            .uri("/v1/items/{id}", id)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(id.toString())
            .jsonPath("$.name").isEqualTo("Arco de Balões")
            .jsonPath("$.category").isEqualTo("Balloon_Arch")
            .jsonPath("$.quantity_available").isEqualTo(3)
            .jsonPath("$.photos[0]").isEqualTo("https://cdn.example.com/balloon-arch.jpg")
    }

    @Test
    fun `should create item successfully`() = runBlocking {
        val request = mapOf(
            "name" to "Mesa Rustica",
            "category" to "Table",
            "quantity_available" to 5,
            "sku" to "MESA-001",
            "active" to true,
            "description" to "Mesa de madeira maciça",
            "unit" to "UNIT",
            "replacement_value" to 500.0,
            "rental_price" to 150.0,
            "photos" to listOf("https://example.com/mesa.jpg")
        )

        webTestClient.post()
            .uri("/v1/items")
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isNotEmpty
            .jsonPath("$.sku").isEqualTo("MESA-001")
            .jsonPath("$.name").isEqualTo("Mesa Rustica")
            .jsonPath("$.active").isEqualTo(true)
    }

    @Test
    fun `should return 409 when creating item with duplicate sku`() = runBlocking {
        // Arrange
        val existingItem = Item(
            id = UUID.randomUUID(),
            sku = "DUPLICATE-SKU",
            name = "Original Item",
            category = ItemCategory.Other,
            description = null,
            quantityAvailable = 1,
            unit = null,
            replacementValue = null,
            rentalPrice = null,
            photos = emptyList(),
            active = true,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        itemRepository.save(existingItem.toEntity())

        val request = mapOf(
            "name" to "New Item",
            "category" to "Table",
            "quantity_available" to 1,
            "sku" to "DUPLICATE-SKU",
             "photos" to listOf("https://example.com/photo.jpg")
        )

        // Act & Assert
        webTestClient.post()
            .uri("/v1/items")
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectHeader().contentType("application/problem+json")
            .expectBody()
            .jsonPath("$.title").isEqualTo("Conflict")
    }

    @Test
    fun `should list items with filters`() = runBlocking {
        // Arrange
        val item1 = Item(
            id = UUID.randomUUID(),
            sku = "A-01",
            name = "Mesa de Vidro",
            category = ItemCategory.Table,
            description = null,
            quantityAvailable = 1,
            unit = null,
            replacementValue = null,
            rentalPrice = null,
            photos = emptyList(),
            active = true,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val item2 = Item(
            id = UUID.randomUUID(),
            sku = "B-01",
            name = "Painel Redondo",
            category = ItemCategory.Panel,
            description = null,
            quantityAvailable = 1,
            unit = null,
            replacementValue = null,
            rentalPrice = null,
            photos = emptyList(),
            active = true,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val item3 = Item(
            id = UUID.randomUUID(),
            sku = "A-02",
            name = "Mesa Infantil",
            category = ItemCategory.Table,
            description = null,
            quantityAvailable = 1,
            unit = null,
            replacementValue = null,
            rentalPrice = null,
            photos = emptyList(),
            active = false,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        itemRepository.save(item1.toEntity())
        itemRepository.save(item2.toEntity())
        itemRepository.save(item3.toEntity())

        // Test Filter by Name (contains)
        webTestClient.get()
            .uri("/v1/items?name=mesa")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.content.length()").isEqualTo(2)
            .jsonPath("$.content[0].name").value<String> { it.contains("Mesa") }

        // Test Filter by Category
        webTestClient.get()
            .uri("/v1/items?category=Panel")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.content.length()").isEqualTo(1)
            .jsonPath("$.content[0].name").isEqualTo("Painel Redondo")

        // Test Filter by Active
        webTestClient.get()
            .uri("/v1/items?active=false")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.content.length()").isEqualTo(1)
            .jsonPath("$.content[0].name").isEqualTo("Mesa Infantil")
    }

    @Test
    fun `should return not found problem when item does not exist`() {
        val id = UUID.randomUUID()

        webTestClient.get()
            .uri("/v1/items/{id}", id)
            .exchange()
            .expectStatus().isNotFound
            .expectHeader().contentType("application/problem+json")
            .expectBody()
            .jsonPath("$.type").isEqualTo("https://lu-alves-decor.com/errors/item-not-found")
            .jsonPath("$.title").isEqualTo("Item not found")
    }
}
