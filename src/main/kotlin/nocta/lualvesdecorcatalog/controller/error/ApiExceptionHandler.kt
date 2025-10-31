package nocta.lualvesdecorcatalog.controller.error

import nocta.lualvesdecorcatalog.core.exception.DecorItemNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(DecorItemNotFoundException::class)
    fun handleDecorItemNotFound(exception: DecorItemNotFoundException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.message ?: "Item not found").apply {
            title = "Item not found"
            type = URI.create("https://lu-alves-decor.com/errors/item-not-found")
        }
    }
}
