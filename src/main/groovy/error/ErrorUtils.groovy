package error


import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import groovy.util.logging.Slf4j
import io.vavr.control.Try
import org.grails.datastore.gorm.GormEntity
import org.springframework.transaction.TransactionStatus

import static error.ErrorContext.badRequest

/**
 * Created by lmuniz on 19/09/15.
 */
@Slf4j
@CompileStatic
class ErrorUtils {
    @CompileDynamic
    static Try validationError(invalidDomain, String message) {
        Try.of {
            def error = badRequest(message)
            error.context.errors = invalidDomain.errors
            throw error
        }
    }

    @CompileDynamic
    static <T> Try<T> withTransaction(Class<? extends GormEntity> Domain, @ClosureParams(value = FromString, options = "TransactionStatus") Closure<Try<T>> operation) {
        try {
            Domain.withTransaction { TransactionStatus status ->
                withTransactionStatus(status, operation)
            }
        } catch (Exception e) {
            Try.failure(ErrorContext.internalError("Unexpected Exception while managing transaction status: ${e.toString()}", e))
        }
    }

    /**
     * Can be used with the @Transactional AST
     * @param operation
     * @return
     */
    static <T> Try<T> withTransactionStatus(TransactionStatus status, Closure<Try<T>> operation) {
        try {
            operation(status).recoverWith { Throwable cause ->
                def error = ErrorContext.wrap(cause)
                log.error("Rolling back due to ${error.message}, context:${error.context}", error.cause)
                status.setRollbackOnly()
                Try.failure(error)
            }
        } catch (Exception e) {
            //try to roll back anyway, in case there's still something to roll back (when closure is not well-behaved, or we have an exception in this method)
            try {
                def error = ErrorContext.internalError("Unexpected Exception while managing transaction status: ${e.toString()}", e)
                log.error("Rolling back due to ${error.toString()}, context:${error.context}", error.cause)
                status.setRollbackOnly()
                Try.failure(error) as Try<T>
            } catch (Exception reallyStubbornException) {
                log.error("Error rolling back  ${reallyStubbornException.toString()}", reallyStubbornException.cause)
            }
        }
    }
}
