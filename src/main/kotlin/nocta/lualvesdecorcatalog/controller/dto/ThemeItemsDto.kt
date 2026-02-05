package nocta.lualvesdecorcatalog.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class ThemeItemsRequest(
    @JsonProperty("item_ids")
    val itemIds: List<UUID>
)

data class ThemeItemsResponse(
    @JsonProperty("item_ids")
    val itemIds: List<UUID>
)
