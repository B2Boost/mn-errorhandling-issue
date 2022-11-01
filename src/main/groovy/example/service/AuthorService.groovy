package example.service


import example.data.AuthorDataService
import example.resource.AuthorResource
import example.transfer.AuthorMarshaller
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import io.vavr.control.Try
import jakarta.inject.Singleton

@Singleton
@Transactional
@CompileStatic
class AuthorService implements AuthorMarshaller {
    private final AuthorDataService authorDataService

    AuthorService(AuthorDataService authorDataService) {
        this.authorDataService = authorDataService
    }

    Try<AuthorResource> get(Long id) {
        authorDataService.tryGet(id).flatMap {
            tryAsResource(it)
        }
    }
}
