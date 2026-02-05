package nocta.lualvesdecorcatalog.repository

import kotlinx.coroutines.reactor.awaitSingle
import nocta.lualvesdecorcatalog.repository.entity.ThemeEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query

interface ThemeRepositoryCustom {
    suspend fun findByFilters(
        name: String?,
        ageGroup: String?,
        active: Boolean?,
        pageable: Pageable
    ): Page<ThemeEntity>
}

class ThemeRepositoryCustomImpl(
    private val template: R2dbcEntityTemplate
) : ThemeRepositoryCustom {

    override suspend fun findByFilters(
        name: String?,
        ageGroup: String?,
        active: Boolean?,
        pageable: Pageable
    ): Page<ThemeEntity> {
        var criteria = Criteria.empty()

        if (!name.isNullOrBlank()) {
            criteria = criteria.and("name").like("%${name.lowercase()}%").ignoreCase(true)
        }
        if (!ageGroup.isNullOrBlank()) {
            criteria = criteria.and("age_group").`is`(ageGroup)
        }
        if (active != null) {
            criteria = criteria.and("active").`is`(active)
        }

        val query = Query.query(criteria).with(pageable)

        val list = template.select(ThemeEntity::class.java)
            .matching(query)
            .all()
            .collectList()
            .awaitSingle()

        val count = template.select(ThemeEntity::class.java)
            .matching(Query.query(criteria))
            .count()
            .awaitSingle()

        return PageImpl(list, pageable, count)
    }
}
