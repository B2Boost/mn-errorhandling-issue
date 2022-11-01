package error


import groovy.transform.CompileStatic

/**
 * Created by lmuniz on 11/05/17.
 * Creates an error context that records the stack trace of the exception
 */
@CompileStatic
class ErrorContextWithStacktrace extends error.ErrorContext {
    ErrorContextWithStacktrace(String message, Throwable cause, int code = 500, String extendedCode = null, Map context = [:]) {
        super(message, cause, code, extendedCode, context, true, true)
    }
}
