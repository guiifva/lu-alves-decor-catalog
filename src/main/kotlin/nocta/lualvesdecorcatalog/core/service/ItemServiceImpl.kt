package nocta.lualvesdecorcatalog.core.service

import java.time.Clock
import java.time.OffsetDateTime
import java.util.UUID
import nocta.lualvesdecorcatalog.core.exception.DomainValidationException
import nocta.lualvesdecorcatalog.core.exception.DuplicateResourceException
import nocta.lualvesdecorcatalog.core.item.CreateItemCommand
import nocta.lualvesdecorcatalog.core.item.Item
import nocta.lualvesdecorcatalog.repository.ItemRepository
import nocta.lualvesdecorcatalog.repository.mapper.toEntity
import nocta.lualvesdecorcatalog.repository.mapper.toModel
import org.springframework.dao.DataIntegrityViolationException
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
}
