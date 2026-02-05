package nocta.lualvesdecorcatalog.controller

import nocta.lualvesdecorcatalog.core.exception.DomainValidationException
import nocta.lualvesdecorcatalog.core.exception.DuplicateResourceException
import nocta.lualvesdecorcatalog.core.exception.ItemNotFoundException
import nocta.lualvesdecorcatalog.core.exception.ThemeNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(DomainValidationException::class)
    fun handleDomainValidation(exception: DomainValidationException): ResponseEntity<ProblemDetail> {
        val problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY)
        problem.title = "Unprocessable Entity"
        problem.detail = exception.message
        problem.setProperty("violations", exception.violations)
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problem)
    }

    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicate(exception: DuplicateResourceException): ResponseEntity<ProblemDetail> {
        val problem = ProblemDetail.forStatus(HttpStatus.CONFLICT)
        problem.title = "Conflict"
        problem.detail = exception.message
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem)
    }

    @ExceptionHandler(ItemNotFoundException::class)
    fun handleItemNotFound(exception: ItemNotFoundException): ResponseEntity<ProblemDetail> {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.message ?: "Item not found")
        problem.title = "Item not found"
        problem.type = URI.create("https://lu-alves-decor.com/errors/item-not-found")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem)
    }

    @ExceptionHandler(ThemeNotFoundException::class)
    fun handleThemeNotFound(exception: ThemeNotFoundException): ResponseEntity<ProblemDetail> {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.message ?: "Theme not found")
        problem.title = "Theme not found"
        problem.type = URI.create("https://lu-alves-decor.com/errors/theme-not-found")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem)
    }
}