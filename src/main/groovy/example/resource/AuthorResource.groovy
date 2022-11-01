package example.resource


import example.model.AuthorModel
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@Introspected
@CompileStatic
class AuthorResource extends AuthorModel {
    Long id
}
