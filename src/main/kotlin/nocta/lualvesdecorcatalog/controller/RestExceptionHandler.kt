package nocta.lualvesdecorcatalog.controller

import nocta.lualvesdecorcatalog.core.exception.DomainValidationException
import nocta.lualvesdecorcatalog.core.exception.DuplicateResourceException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

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
}
