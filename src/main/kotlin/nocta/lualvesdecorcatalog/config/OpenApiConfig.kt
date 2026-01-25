package nocta.lualvesdecorcatalog.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Lu Alves Decor Catalog API")
                    .version("1.0.0")
                    .description("API for managing decoration items and themes for contracts.")
            )
    }
}
