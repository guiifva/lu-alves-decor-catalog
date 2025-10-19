package nocta.lualvesdecorcatalog.repository

import nocta.lualvesdecorcatalog.repository.entity.ThemeEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ThemeRepository : CoroutineCrudRepository<ThemeEntity, Long>
