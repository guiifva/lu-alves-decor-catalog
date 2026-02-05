package nocta.lualvesdecorcatalog.repository

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import kotlinx.coroutines.reactor.awaitSingle
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.repository.entity.ItemEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.r2dbc.core.DatabaseClient.GenericExecuteSpec
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import java.util.function.BiFunction

class ItemRepositoryCustomImpl(
    private val template: R2dbcEntityTemplate
) : ItemRepositoryCustom {

    override suspend fun findByFilters(
        name: String?,
        category: ItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<ItemEntity> {
        val criteria = buildCriteria(name, category, active)
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

    override suspend fun findByThemeIdAndFilters(
        themeId: UUID,
        name: String?,
        category: ItemCategory?,
        active: Boolean?,
        pageable: Pageable
    ): Page<ItemEntity> {
        val client = template.databaseClient

        // Base Query construction
        val whereClause = StringBuilder("WHERE ti.theme_id = :themeId")
        if (!name.isNullOrBlank()) whereClause.append(" AND lower(i.name) LIKE :name")
        if (category != null) whereClause.append(" AND i.category = :category")
        if (active != null) whereClause.append(" AND i.active = :active")

        val selectSql = """
            SELECT i.* FROM items i
            JOIN theme_items ti ON i.id = ti.item_id
            $whereClause
            ORDER BY ${buildSortClause(pageable)}
            LIMIT :limit OFFSET :offset
        """

        val countSql = """
            SELECT count(i.id) FROM items i
            JOIN theme_items ti ON i.id = ti.item_id
            $whereClause
        """

        // Bind parameters
        val bindParams: (GenericExecuteSpec) -> GenericExecuteSpec = { spec ->
            var s = spec.bind("themeId", themeId)
            if (!name.isNullOrBlank()) s = s.bind("name", "%${name.lowercase()}%")
            if (category != null) s = s.bind("category", category.name)
            if (active != null) s = s.bind("active", active)
            s
        }

        val items = bindParams(client.sql(selectSql))
            .bind("limit", pageable.pageSize)
            .bind("offset", pageable.offset)
            .map(MAPPING_FUNCTION)
            .all()
            .collectList()
            .awaitSingle()

        val total = bindParams(client.sql(countSql))
            .map { row -> row.get(0, java.lang.Long::class.java)?.toLong() ?: 0L }
            .one()
            .awaitSingle()

        return PageImpl(items, pageable, total)
    }

    private fun buildCriteria(name: String?, category: ItemCategory?, active: Boolean?): Criteria {
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
        return criteria
    }

    private fun buildSortClause(pageable: Pageable): String {
        if (!pageable.sort.isSorted) return "i.name ASC"
        
        return pageable.sort.joinToString(", ") { order ->
            val prop = when (order.property) {
                "name" -> "i.name"
                "category" -> "i.category"
                else -> "i.name"
            }
            "$prop ${order.direction}"
        }
    }

    companion object {
        private val MAPPING_FUNCTION = BiFunction<Row, RowMetadata, ItemEntity> { row, _ ->
            ItemEntity(
                id = row.get("id", UUID::class.java),
                sku = row.get("sku", String::class.java),
                name = row.get("name", String::class.java) ?: "",
                category = row.get("category", String::class.java) ?: "",
                description = row.get("description", String::class.java),
                quantityAvailable = row.get("quantity_available", Integer::class.java)?.toInt() ?: 0,
                unit = row.get("unit", String::class.java),
                replacementValue = row.get("replacement_value", BigDecimal::class.java),
                rentalPrice = row.get("rental_price", BigDecimal::class.java),
                photos = row.get("photos", String::class.java),
                active = row.get("active", java.lang.Boolean::class.java)?.booleanValue() ?: true,
                createdAt = row.get("created_at", OffsetDateTime::class.java)!!,
                updatedAt = row.get("updated_at", OffsetDateTime::class.java)!!
            )
        }
    }
}