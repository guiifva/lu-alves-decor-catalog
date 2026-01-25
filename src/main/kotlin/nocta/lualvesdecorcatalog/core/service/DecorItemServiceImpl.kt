package nocta.lualvesdecorcatalog.core.service

import nocta.lualvesdecorcatalog.controller.dto.CreateDecorItemRequest
import nocta.lualvesdecorcatalog.core.entity.DecorItem
import nocta.lualvesdecorcatalog.core.entity.DecorItemCategory
import nocta.lualvesdecorcatalog.core.exception.DecorItemNotFoundException
import nocta.lualvesdecorcatalog.core.exception.SkuAlreadyExistsException
import nocta.lualvesdecorcatalog.repository.DecorItemRepository
import nocta.lualvesdecorcatalog.repository.mapper.toDomain
import nocta.lualvesdecorcatalog.repository.mapper.toEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class DecorItemServiceImpl(
    private val decorItemRepository: DecorItemRepository
) : DecorItemService {

    override suspend fun create(request: CreateDecorItemRequest): DecorItem {
        val entity = request.toDomain().toEntity()
        try {
            return decorItemRepository.save(entity).toDomain()
        } catch (e: org.springframework.dao.DuplicateKeyException) {
            if (request.sku != null) {
                throw SkuAlreadyExistsException(request.sku)
            }
            throw e
        }
    }

    override suspend fun getById(id: UUID): DecorItem {
        return decorItemRepository.findById(id)?.toDomain()
            ?: throw DecorItemNotFoundException(id)
    }

    override suspend fun list(
        name: String?,
        category: DecorItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<DecorItem> {
        return decorItemRepository.findByFilters(name, category, active, pageable)
            .map { it.toDomain() }
    }
}