package error

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@Introspected
@CompileStatic
class ErrorResource {
    int code
    String message
    String extendedCode
    Map context

    static ErrorResource of(ErrorContext error) {
        new ErrorResource(
                code: error.code,
                message: error.message,
                extendedCode: error.extendedCode,
                context: error.context,
        )
    }
}
