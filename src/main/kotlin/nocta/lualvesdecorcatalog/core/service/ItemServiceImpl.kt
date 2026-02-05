package nocta.lualvesdecorcatalog.core.service

import java.time.Clock
import java.time.OffsetDateTime
import java.util.UUID
import nocta.lualvesdecorcatalog.core.exception.DomainValidationException
import nocta.lualvesdecorcatalog.core.exception.DuplicateResourceException
import nocta.lualvesdecorcatalog.core.exception.ItemNotFoundException
import nocta.lualvesdecorcatalog.core.item.CreateItemCommand
import nocta.lualvesdecorcatalog.core.item.Item
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.core.item.PatchItemCommand
import nocta.lualvesdecorcatalog.core.item.UpdateItemCommand
import nocta.lualvesdecorcatalog.repository.ItemRepository
import nocta.lualvesdecorcatalog.repository.mapper.toEntity
import nocta.lualvesdecorcatalog.repository.mapper.toModel
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemServiceImpl(
    private val itemRepository: ItemRepository,
    private val clock: Clock,
) : ItemService {
    @Transactional
    override suspend fun create(command: CreateItemCommand): Item {
        val now = OffsetDateTime.now(clock)
        val item = try {
            command.toItem(now)
        } catch (ex: IllegalArgumentException) {
            throw DomainValidationException(listOf(ex.message ?: "Invalid item data."))
        }

        if (command.sku != null && itemRepository.existsBySku(command.sku)) {
            throw DuplicateResourceException("Item with sku '${command.sku}' already exists.")
        }

        val itemWithId = item.withId(UUID.randomUUID())
        val entity = itemWithId.toEntity()

        val saved = try {
            itemRepository.save(entity)
        } catch (ex: DataIntegrityViolationException) {
            if (command.sku != null) {
                throw DuplicateResourceException("Item with sku '${command.sku}' already exists.")
            }
            throw ex
        }

        return saved.toModel()
    }

    override suspend fun getById(id: UUID): Item {
        return itemRepository.findById(id)?.toModel()
            ?: throw ItemNotFoundException(id)
    }

    override suspend fun list(
        name: String?,
        category: ItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<Item> {
        return itemRepository.findByFilters(name, category, active, pageable)
            .map { it.toModel() }
    }

    @Transactional
    override suspend fun update(id: UUID, command: UpdateItemCommand): Item {
        val existingItem = getById(id)

        if (command.sku != null && command.sku != existingItem.sku && itemRepository.existsBySku(command.sku)) {
            throw DuplicateResourceException("Item with sku '${command.sku}' already exists.")
        }

        val updatedItem = try {
            existingItem.copy(
                sku = command.sku,
                name = command.name,
                category = command.category,
                description = command.description,
                quantityAvailable = command.quantityAvailable,
                unit = command.unit,
                replacementValue = command.replacementValue,
                rentalPrice = command.rentalPrice,
                photos = command.photos,
                active = command.active,
                updatedAt = OffsetDateTime.now(clock)
            )
        } catch (ex: IllegalArgumentException) {
            throw DomainValidationException(listOf(ex.message ?: "Invalid item data."))
        }

        return itemRepository.save(updatedItem.toEntity()).toModel()
    }

    @Transactional
    override suspend fun patch(id: UUID, command: PatchItemCommand): Item {
        val existingItem = getById(id)

        if (command.sku != null && command.sku != existingItem.sku && itemRepository.existsBySku(command.sku)) {
            throw DuplicateResourceException("Item with sku '${command.sku}' already exists.")
        }

        val updatedItem = try {
            existingItem.copy(
                sku = command.sku ?: existingItem.sku,
                name = command.name ?: existingItem.name,
                category = command.category ?: existingItem.category,
                description = command.description ?: existingItem.description,
                quantityAvailable = command.quantityAvailable ?: existingItem.quantityAvailable,
                unit = command.unit ?: existingItem.unit,
                replacementValue = command.replacementValue ?: existingItem.replacementValue,
                rentalPrice = command.rentalPrice ?: existingItem.rentalPrice,
                photos = command.photos ?: existingItem.photos,
                active = command.active ?: existingItem.active,
                updatedAt = OffsetDateTime.now(clock)
            )
        } catch (ex: IllegalArgumentException) {
            throw DomainValidationException(listOf(ex.message ?: "Invalid item data."))
        }

        return itemRepository.save(updatedItem.toEntity()).toModel()
    }

    @Transactional
    override suspend fun delete(id: UUID) {
        val existingItem = getById(id)
        itemRepository.deleteById(existingItem.id!!)
    }
}