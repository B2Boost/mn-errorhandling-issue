package error

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.vavr.control.Try

@CompileStatic
class VavrTypes {
    private static final Closure<ErrorContext> DEFAULT_ERRORMAPPER = { Throwable t ->
        ErrorContext.wrap(t.message, t)
    }

    static <T> Try<T> asTry(
            Single<T> toConvert,
            @ClosureParams(value = FromString, options = ["java.lang.Throwable"])
                    Closure<ErrorContext> errorMapper = null) {
        Try<T> res = null
        if (errorMapper == null) {
            errorMapper = DEFAULT_ERRORMAPPER
        }
        try {
            toConvert.doOnError { Throwable t ->
                res = Try.failure(errorMapper(t))
            }.doOnSuccess { T nugget ->
                res = Try.success(nugget)
            }.blockingGet()
        } catch (Error e) {
            throw e
        } catch (RuntimeException e) {
            //ignore, as we have already assigned the error
        }
        res
    }

    static <T> Try<List<T>> asTryOfList(
            Flowable<T> toConvert,
            @ClosureParams(value = FromString, options = ["java.lang.Throwable"])
                    Closure<ErrorContext> errorMapper = null) {
        List<T> acc = []
        Try<List<T>> res = Try.success(acc)
        if (errorMapper == null) {
            errorMapper = DEFAULT_ERRORMAPPER
        }
        try {
            toConvert.doOnError { Throwable t ->
                res = Try.failure(errorMapper(t))
            }.blockingForEach { T next ->
                acc << next
            }
        } catch (Error e) {
            throw e
        } catch (RuntimeException e) {
            //ignore, as we have already assigned the error
        }
        res
    }

    static <T> Try<List<T>> asTryOfList(List<Try<T>> toConvert) {
        Try.sequence(toConvert).map{it.toJavaList()}
    }
}
