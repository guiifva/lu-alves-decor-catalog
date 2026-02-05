package nocta.lualvesdecorcatalog.core.service

import java.time.Clock
import java.time.OffsetDateTime
import java.util.UUID
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import nocta.lualvesdecorcatalog.core.entity.CreateThemeCommand
import nocta.lualvesdecorcatalog.core.entity.PatchThemeCommand
import nocta.lualvesdecorcatalog.core.entity.Theme
import nocta.lualvesdecorcatalog.core.entity.UpdateThemeCommand
import nocta.lualvesdecorcatalog.core.exception.DomainValidationException
import nocta.lualvesdecorcatalog.core.exception.DuplicateResourceException
import nocta.lualvesdecorcatalog.core.exception.ItemNotFoundException
import nocta.lualvesdecorcatalog.core.exception.ThemeNotFoundException
import nocta.lualvesdecorcatalog.core.item.Item
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.repository.ItemRepository
import nocta.lualvesdecorcatalog.repository.ThemeItemRepository
import nocta.lualvesdecorcatalog.repository.ThemeRepository
import nocta.lualvesdecorcatalog.repository.mapper.toEntity
import nocta.lualvesdecorcatalog.repository.mapper.toModel
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ThemeServiceImpl(
    private val themeRepository: ThemeRepository,
    private val themeItemRepository: ThemeItemRepository,
    private val itemRepository: ItemRepository,
    private val clock: Clock
) : ThemeService {

    private fun String.toSlug(): String {
        val normalized = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
        return normalized.lowercase()
            .replace(Regex("[\\p{InCombiningDiacriticalMarks}]"), "")
            .replace(Regex("[^a-z0-9\\s]"), "")
            .trim()
            .replace(Regex("\\s+"), "-")
    }

    @Transactional
    override suspend fun create(command: CreateThemeCommand): Theme {
        if (themeRepository.existsByNameIgnoreCase(command.name)) {
            throw DuplicateResourceException("Theme with name '${command.name}' already exists.")
        }
        val slug = command.name.toSlug()
        if (themeRepository.existsBySlug(slug)) {
             throw DuplicateResourceException("Theme with slug '$slug' already exists.")
        }
        
        if (command.priceMin != null && command.priceMax != null && command.priceMin > command.priceMax) {
            throw DomainValidationException(listOf("Price min must be less than or equal to price max."))
        }

        val now = OffsetDateTime.now(clock)
        val theme = Theme(
            id = UUID.randomUUID(),
            slug = slug,
            name = command.name,
            description = command.description,
            ageGroup = command.ageGroup,
            priceMin = command.priceMin,
            priceMax = command.priceMax,
            images = command.images ?: emptyList(),
            active = command.active ?: true,
            createdAt = now,
            updatedAt = now
        )

        return try {
            themeRepository.save(theme.toEntity()).toModel()
        } catch (ex: DataIntegrityViolationException) {
            throw DuplicateResourceException("Theme with name '${command.name}' or slug '$slug' already exists.")
        }
    }

    override suspend fun getById(id: UUID, includeItems: Boolean): Theme {
        val theme = themeRepository.findById(id)?.toModel()
            ?: throw ThemeNotFoundException(id)

        if (includeItems) {
            val itemIds = themeItemRepository.findItemIdsByThemeId(id).toList()
            if (itemIds.isNotEmpty()) {
                val items = itemRepository.findAllById(itemIds).toList().map { it.toModel() }
                return theme.copy(items = items)
            }
        }
        return theme
    }

    override suspend fun list(
        name: String?,
        ageGroup: String?,
        active: Boolean?,
        pageable: Pageable
    ): Page<Theme> {
        return themeRepository.findByFilters(name, ageGroup, active, pageable)
            .map { it.toModel() }
    }

    @Transactional
    override suspend fun update(id: UUID, command: UpdateThemeCommand): Theme {
        val existing = getById(id)
        
        if (!existing.name.equals(command.name, ignoreCase = true)) {
             if (themeRepository.existsByNameIgnoreCase(command.name)) {
                 throw DuplicateResourceException("Theme with name '${command.name}' already exists.")
             }
        }
        
        val slug = command.name.toSlug()
        if (slug != existing.slug) {
            if (themeRepository.existsBySlug(slug)) {
                 throw DuplicateResourceException("Theme with slug '$slug' already exists.")
            }
        }

        if (command.priceMin != null && command.priceMax != null && command.priceMin > command.priceMax) {
             throw DomainValidationException(listOf("Price min must be less than or equal to price max."))
        }

        val updated = existing.copy(
            slug = slug,
            name = command.name,
            description = command.description,
            ageGroup = command.ageGroup,
            priceMin = command.priceMin,
            priceMax = command.priceMax,
            images = command.images,
            active = command.active,
            updatedAt = OffsetDateTime.now(clock)
        )
        return try {
            themeRepository.save(updated.toEntity()).toModel()
        } catch (ex: DataIntegrityViolationException) {
             throw DuplicateResourceException("Theme with name '${command.name}' or slug '$slug' already exists.")
        }
    }

    @Transactional
    override suspend fun patch(id: UUID, command: PatchThemeCommand): Theme {
        val existing = getById(id)
        
        val newName = command.name ?: existing.name
        val newSlug = if (command.name != null) command.name.toSlug() else existing.slug!!
        
        if (command.name != null && !existing.name.equals(command.name, ignoreCase = true)) {
             if (themeRepository.existsByNameIgnoreCase(command.name)) {
                 throw DuplicateResourceException("Theme with name '${command.name}' already exists.")
             }
        }
        
        if (newSlug != existing.slug) {
             if (themeRepository.existsBySlug(newSlug)) {
                 throw DuplicateResourceException("Theme with slug '$newSlug' already exists.")
            }
        }
        
        val priceMin = command.priceMin ?: existing.priceMin
        val priceMax = command.priceMax ?: existing.priceMax
        
        if (priceMin != null && priceMax != null && priceMin > priceMax) {
             throw DomainValidationException(listOf("Price min must be less than or equal to price max."))
        }

        val updated = existing.copy(
            slug = newSlug,
            name = newName,
            description = command.description ?: existing.description,
            ageGroup = command.ageGroup ?: existing.ageGroup,
            priceMin = priceMin,
            priceMax = priceMax,
            images = command.images ?: existing.images,
            active = command.active ?: existing.active,
            updatedAt = OffsetDateTime.now(clock)
        )
        return try {
            themeRepository.save(updated.toEntity()).toModel()
        } catch (ex: DataIntegrityViolationException) {
            throw DuplicateResourceException("Theme with name '$newName' or slug '$newSlug' already exists.")
        }
    }

    @Transactional
    override suspend fun delete(id: UUID) {
        val existing = getById(id)
        themeRepository.deleteById(existing.id!!)
    }

    @Transactional
    override suspend fun addItems(themeId: UUID, itemIds: List<UUID>) {
        getById(themeId)
        
        val foundItems = itemRepository.findAllById(itemIds).toList()
        val foundIds = foundItems.mapNotNull { it.id }.toSet()
        val missing = itemIds.distinct().filter { !foundIds.contains(it) }
        
        if (missing.isNotEmpty()) {
            throw ItemNotFoundException(missing.first())
        }

        coroutineScope {
            itemIds.distinct().map { itemId ->
                launch { themeItemRepository.addLink(themeId, itemId) }
            }.joinAll()
        }
    }

    @Transactional
    override suspend fun removeItem(themeId: UUID, itemId: UUID) {
        themeItemRepository.deleteByThemeIdAndItemId(themeId, itemId)
    }

    override suspend fun listItems(
        themeId: UUID,
        name: String?,
        category: ItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<Item> {
        getById(themeId)
        return itemRepository.findByThemeIdAndFilters(themeId, name, category, active, pageable)
            .map { it.toModel() }
    }
}