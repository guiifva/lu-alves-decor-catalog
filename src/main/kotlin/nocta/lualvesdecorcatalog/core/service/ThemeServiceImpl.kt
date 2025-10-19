package nocta.lualvesdecorcatalog.core.service

import kotlinx.coroutines.flow.toList
import nocta.lualvesdecorcatalog.core.entity.Theme
import nocta.lualvesdecorcatalog.repository.ItemRepository
import nocta.lualvesdecorcatalog.repository.ThemeRepository
import nocta.lualvesdecorcatalog.repository.mapper.toEntity
import nocta.lualvesdecorcatalog.repository.mapper.toModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ThemeServiceImpl(
    private val themeRepository: ThemeRepository,
    private val itemRepository: ItemRepository
) : ThemeService {
    @Transactional
    override suspend fun create(theme: Theme): Theme {
        val themeEntity = theme.toEntity()
        val savedTheme = themeRepository.save(themeEntity)
        val themeId = savedTheme.id
            ?: throw IllegalStateException("Theme persisted without generated identifier.")

        val savedItems = theme.items
            .map { it.toEntity(themeId) }
            .let { itemRepository.saveAll(it).toList() }

        return savedTheme.toModel(savedItems.map { it.toModel() })
    }
}
