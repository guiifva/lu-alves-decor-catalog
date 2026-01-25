package nocta.lualvesdecorcatalog.repository

import nocta.lualvesdecorcatalog.core.entity.DecorItemCategory
import nocta.lualvesdecorcatalog.repository.entity.DecorItemEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle

interface DecorItemRepositoryCustom {
    suspend fun findByFilters(
        name: String?,
        category: DecorItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<DecorItemEntity>
}

class DecorItemRepositoryCustomImpl(
    private val template: R2dbcEntityTemplate
) : DecorItemRepositoryCustom {

    override suspend fun findByFilters(
        name: String?,
        category: DecorItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<DecorItemEntity> {
        var criteria = Criteria.empty()

        if (!name.isNullOrBlank()) {
            criteria = criteria.and("name").like("%${name.lowercase()}%").ignoreCase(true)
        }
        if (category != null) {
            criteria = criteria.and("category").`is`(category.name)
        }
        if (active != null) {
            criteria = criteria.and("active").`is`(active)
        }

        val query = Query.query(criteria).with(pageable)

        val list = template.select(DecorItemEntity::class.java)
            .matching(query)
            .all()
            .collectList()
            .awaitSingle()

        val count = template.select(DecorItemEntity::class.java)
            .matching(Query.query(criteria))
            .count()
            .awaitSingle()

        return PageImpl(list, pageable, count)
    }
}
