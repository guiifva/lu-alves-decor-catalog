package nocta.lualvesdecorcatalog.repository

import kotlinx.coroutines.reactor.awaitSingle
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.repository.entity.ItemEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query

interface ItemRepositoryCustom {
    suspend fun findByFilters(
        name: String?,
        category: ItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<ItemEntity>
}

class ItemRepositoryCustomImpl(
    private val template: R2dbcEntityTemplate
) : ItemRepositoryCustom {

    override suspend fun findByFilters(
        name: String?,
        category: ItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<ItemEntity> {
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

        val list = template.select(ItemEntity::class.java)
            .matching(query)
            .all()
            .collectList()
            .awaitSingle()

        val count = template.select(ItemEntity::class.java)
            .matching(Query.query(criteria))
            .count()
            .awaitSingle()

        return PageImpl(list, pageable, count)
    }
}
