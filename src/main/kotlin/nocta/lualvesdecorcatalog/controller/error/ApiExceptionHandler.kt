package nocta.lualvesdecorcatalog.controller.error

import nocta.lualvesdecorcatalog.core.exception.DecorItemNotFoundException
import nocta.lualvesdecorcatalog.core.exception.SkuAlreadyExistsException
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

    @ExceptionHandler(SkuAlreadyExistsException::class)
    fun handleSkuAlreadyExists(exception: SkuAlreadyExistsException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.message ?: "SKU already exists").apply {
            title = "SKU already exists"
            type = URI.create("https://lu-alves-decor.com/errors/sku-already-exists")
        }
    }
}
