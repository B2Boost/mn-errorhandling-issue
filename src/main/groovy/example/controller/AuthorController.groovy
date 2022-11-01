package example.controller

import error.ErrorContextWithoutStacktrace
import example.resource.AuthorResource
import example.service.AuthorService
import groovy.transform.CompileStatic
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.validation.Validated

import java.net.http.HttpRequest

import static io.micronaut.http.HttpResponse.ok

@CompileStatic
@Validated
@Controller("/author")
class AuthorController implements ControllerResponses {
    private final AuthorService authorService

    AuthorController(AuthorService authorService) {
        this.authorService = authorService
    }

//    @Get(uri = "/{id}")
//    @Secured("ROLE_VIEW")
//    HttpResponse<AuthorResource> show(Long id) {
//        tryRespond(authorService.get(id)) {
//            ok(it)
//        }
//    }

    @Get(uri = "/{id}")
    @Secured("ROLE_VIEW")
    HttpResponse<AuthorResource> show(Long id) {
        try {
            ok(authorService.get(id).get())
        } catch (Exception e) {
            e.printStackTrace()
            throw e
        }
    }

    @Error(exception = ErrorContextWithoutStacktrace, global = true)
    HttpResponse<ErrorContextWithoutStacktrace> onErrorContext(HttpRequest request, ErrorContextWithoutStacktrace error) {
        println "*****************"
        println "*****************"
        println "*****************"
        HttpResponse.<ErrorContextWithoutStacktrace>status(HttpStatus.valueOf(error.code)).body(error)
    }
}
