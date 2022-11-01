package error


import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

import static error.ErrorCodes.*

/**
 * Created by lmuniz on 11/05/17.
 *
 */
@CompileStatic
@Introspected
class ErrorContext extends RuntimeException {
    int code
    String extendedCode
    Map context

    ErrorContext(String message, Throwable cause, int code, String extendedCode, Map context, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace)
        this.code = code
        this.extendedCode = extendedCode
        this.context = context
    }

    //--------------------------------------------------------------------
    //- With stack trace
    //--------------------------------------------------------------------
    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext internalError(String message, Throwable cause) {
        new ErrorContextWithStacktrace(message, cause)
    }

    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext notFoundWithStack(String message, Throwable cause, Map context = [:]) {
        new ErrorContextWithStacktrace(message, cause, NOT_FOUND, null, context)
    }

    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext badRequestWithStack(String message, Throwable cause, Map context = [:]) {
        new ErrorContextWithStacktrace(message, cause, BAD_REQUEST, null, context)
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    static ErrorContext methodNotAllowed(String message, Throwable cause = null, Map context = [:]) {
        new ErrorContextWithStacktrace(message, cause, 405, null, context)
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    static ErrorContext conflict(String message, Throwable cause = null, Map context = [:]) {
        new ErrorContextWithStacktrace(message, cause, CONFLICT, null, context)
    }

    //--------------------------------------------------------------------
    //- Without stack trace
    //--------------------------------------------------------------------
    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext notFound(String message, Throwable cause, Map context = [:]) {
        new error.ErrorContextWithoutStacktrace(message, cause, NOT_FOUND, null, context)
    }

    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext badRequest(String message, Throwable cause, Map context = [:]) {
        new error.ErrorContextWithoutStacktrace(message, cause, BAD_REQUEST, null, context)
    }

    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext notFound(String message, Map context = [:]) {
        notFound(message, null, context)
    }

    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext badRequest(String message, Map context = [:]) {
        badRequest(message, null, context)
    }

    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext forbidden(String message, Map context = [:]) {
        new ErrorContextWithStacktrace("Access denied", null, error.ErrorCodes.FORBIDDEN, null, context)
    }



    // --- 3xx Redirection ---
    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext notModified(String message, Map context = [:]) {
        new error.ErrorContextWithoutStacktrace(message, null, NOT_MODIFIED, null, context)
    }

    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext generic(String message, int code, String extendedCode, Map context = [:]) {
        new error.ErrorContextWithoutStacktrace(message, null, code, extendedCode, context)
    }

    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext genericWithStackTrace(String message, int code, String extendedCode, Map context = [:]) {
        new ErrorContextWithStacktrace(message, null, code, extendedCode, context)
    }

    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    public static ErrorContext generic(String message, Throwable cause, int code, String extendedCode, Map context = [:]) {
        new ErrorContextWithStacktrace(message, cause, code, extendedCode, context)
    }

    /**
     * We use duck typing to avoid including the grails class grails.validation.Validateable in the dependencies
     */
    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnnecessaryPublicModifier"])
    @CompileDynamic
    //We use duck typing to avoid including the grails class grails.validation.Validateable in the dependencies
    public static ErrorContext validationError(validateable) {
        def errors = validateable.errors
        badRequest("${errors.objectName} invalid!", [errors: errors.allErrors.collect { it.toString() }])
    }

    @SuppressWarnings("GrUnnecessaryPublicModifier")
    public static ErrorContext wrap(String message, Throwable t) {
        if (t instanceof ErrorContext) {
            t as ErrorContext
        } else {
            internalError("${message}: ${t.message}", t)
        }
    }

    @SuppressWarnings("GrUnnecessaryPublicModifier")
    public static ErrorContext wrap(Throwable t) {
        wrap("Unexpected Throwable ${t.getClass().name} Should be an ErrorContext.", t)
    }

    @SuppressWarnings("GrUnnecessaryPublicModifier")
    public static ErrorContext throwWrapped(String message, Throwable t) {
        throw wrap(message, t)
    }
}
