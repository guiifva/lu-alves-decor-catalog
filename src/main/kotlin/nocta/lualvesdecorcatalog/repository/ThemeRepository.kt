package nocta.lualvesdecorcatalog.repository

import java.util.UUID
import nocta.lualvesdecorcatalog.repository.entity.ThemeEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ThemeRepository : CoroutineCrudRepository<ThemeEntity, UUID>, ThemeRepositoryCustom {
    suspend fun existsByNameIgnoreCase(name: String): Boolean
    suspend fun existsBySlug(slug: String): Boolean
}
