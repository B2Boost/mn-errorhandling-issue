package example.model

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected


@Introspected
@CompileStatic
class AuthorModel {
    String name
}
