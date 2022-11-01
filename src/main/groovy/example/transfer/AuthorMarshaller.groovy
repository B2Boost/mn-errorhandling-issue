package example.transfer

import example.entity.Author
import example.model.AuthorModel
import example.resource.AuthorResource
import groovy.transform.CompileStatic
import io.vavr.control.Try


@CompileStatic
trait AuthorMarshaller {

    Try<Author> tryAsEntity(AuthorModel author, Long id = null) {
        Try.of {
            asEntity(author, id)
        }
    }

    Try<AuthorModel> tryAsEntity(Author entity) {
        Try.of {
            asEntity(entity)
        }
    }

    Try<AuthorResource> tryAsResource(Author entity) {
        Try.of {
            asResource(entity)
        }
    }

    Author asEntity(AuthorModel author, long id) {
        new Author(
                name: author.name,
                id: id
        )
    }

    AuthorModel asEntity(Author entity) {
        new AuthorModel(name: entity.name)
    }

    AuthorResource asResource(Author entity) {
        new AuthorResource(
                name: entity.name,
                id: entity.id
        )
    }


}