package error

import groovy.transform.CompileStatic

/**
 * Created by lmuniz on 11/05/17.
 * Creates an error context that does not contain a stack trace. This kind of context is used to throw an exception that
 * carries business semantics, but the stack trace is not important. Used in conjunction with Try of the vavr library.
 * The Try.of method will only return a failure if an exception is thrown in the block.
 */
@CompileStatic
class ErrorContextWithoutStacktrace extends ErrorContext {
    ErrorContextWithoutStacktrace(String message, Throwable cause, int code = 500, String extendedCode = null, Map context = [:]) {
        super(message, cause, code, extendedCode, context, false, false)
    }
}
