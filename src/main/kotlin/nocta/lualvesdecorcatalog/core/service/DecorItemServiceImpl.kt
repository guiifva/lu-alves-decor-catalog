package nocta.lualvesdecorcatalog.core.service

import nocta.lualvesdecorcatalog.core.exception.DecorItemNotFoundException
import nocta.lualvesdecorcatalog.core.entity.DecorItem
import nocta.lualvesdecorcatalog.repository.DecorItemRepository
import nocta.lualvesdecorcatalog.repository.mapper.toDomain
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DecorItemServiceImpl(
    private val decorItemRepository: DecorItemRepository
) : DecorItemService {
    override suspend fun getById(id: UUID): DecorItem {
        return decorItemRepository.findById(id)?.toDomain()
            ?: throw DecorItemNotFoundException(id)
    }
}
