package nocta.lualvesdecorcatalog.controller

import kotlinx.coroutines.runBlocking
import nocta.lualvesdecorcatalog.core.entity.DecorItem
import nocta.lualvesdecorcatalog.core.entity.DecorItemCategory
import nocta.lualvesdecorcatalog.core.entity.DecorItemUnit
import nocta.lualvesdecorcatalog.repository.DecorItemRepository
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
class DecorItemControllerIT @Autowired constructor(
    private val webTestClient: WebTestClient,
    private val decorItemRepository: DecorItemRepository
) {

    @BeforeEach
    fun setUp() = runBlocking {
        decorItemRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() = runBlocking {
        decorItemRepository.deleteAll()
    }

    @Test
    fun `should return decor item when it exists`() = runBlocking {
        val id = UUID.randomUUID()
        val now = OffsetDateTime.parse("2024-01-01T10:00:00Z")
        val decorItem = DecorItem(
            id = id,
            sku = "SKU-123",
            name = "Arco de Balões",
            category = DecorItemCategory.BALLOON_ARCH,
            description = "Arco completo em tons pastel",
            quantityAvailable = 3,
            unit = DecorItemUnit.SET,
            replacementValue = BigDecimal("1500.00"),
            rentalPrice = BigDecimal("450.00"),
            photos = listOf("https://cdn.example.com/balloon-arch.jpg"),
            active = true,
            createdAt = now,
            updatedAt = now
        )
        decorItemRepository.save(decorItem.toEntity())

        webTestClient.get()
            .uri("/v1/items/{id}", id)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(id.toString())
            .jsonPath("$.name").isEqualTo("Arco de Balões")
            .jsonPath("$.category").isEqualTo("BALLOON_ARCH")
            .jsonPath("$.quantity_available").isEqualTo(3)
            .jsonPath("$.photos[0]").isEqualTo("https://cdn.example.com/balloon-arch.jpg")
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
