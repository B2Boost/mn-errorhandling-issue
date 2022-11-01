package error


import groovy.transform.CompileStatic
import io.vavr.control.Try
import org.springframework.validation.Errors

@CompileStatic
trait ValidationSupport {
    abstract boolean validate()

    abstract Errors getErrors()


    Try<Void> tryValidate() {
        Try.of {
            if (validate()) {
                null as Void
            } else {
                throw ErrorContext.validationError(this)
            }
        }
    }
}