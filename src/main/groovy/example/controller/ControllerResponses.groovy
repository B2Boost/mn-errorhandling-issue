package example.controller

import error.ErrorContext
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import io.micronaut.http.HttpResponseFactory
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.vavr.control.Try

import java.util.function.Function

@CompileStatic
trait ControllerResponses {
    def <T> MutableHttpResponse<T> tryRespond(Try<T> attempt, @ClosureParams(value = FromString, options = "T") Closure<MutableHttpResponse<T>> handler) {
        attempt.map {
            handler(it)
        }.recover(ErrorContext, { ErrorContext error ->
            HttpResponseFactory.INSTANCE
                    .status(HttpStatus.valueOf(error.code))
                    .body(error)
        } as Function).recover(Throwable, { Throwable uncaught ->
            def error = ErrorContext.wrap("Uncaught exception of type ${uncaught.getClass().name}: ${uncaught.message}", uncaught)
            HttpResponseFactory.INSTANCE
                    .<ErrorContext>status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error)
        } as Function).get()
    }

}