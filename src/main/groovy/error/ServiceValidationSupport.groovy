package error

import io.vavr.control.Try

import javax.validation.ValidationException

trait ServiceValidationSupport {
    def <C> Try<C> handleValidationException(Closure<C> process) {
        Try.of {
            try {
                process()
            } catch (ValidationException e) {
                throw ErrorContext.badRequest(e.message, [errors: e.errors.allErrors.collect { it.toString() }])
            }
        }
    }
}