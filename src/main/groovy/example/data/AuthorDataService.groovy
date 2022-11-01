package example.data

import error.ErrorCodes
import error.ErrorContext
import error.ServiceValidationSupport
import example.entity.Author
import example.model.AuthorModel
import grails.gorm.services.Service
import groovy.transform.CompileStatic
import io.vavr.control.Try

//tag::generated[]
@Service(Author)
@CompileStatic
abstract class AuthorDataService implements ServiceValidationSupport {
    abstract List<Author> list()

    abstract List<Author> list(Map args)

    abstract Author save(Author author)

    abstract void delete(Long id)

    abstract void delete(Long id, Map args)

    abstract Author get(Long id)

    abstract Author findByName(String name)

    abstract Integer count()
//end::generated[]


    Try<Author> tryFindByName(String name) {
        Try.of {
            def author = findByName(name)
            if (author) {
                author
            } else {
                throw ErrorContext.notFound("Author with name $name not found.")
            }
        }
    }

    Try<Author> tryGet(Long id) {
        Try.of {
            def author = get(id)
            if (author) {
                author
            } else {
                throw ErrorContext.notFound("Author with id $id not found.")
            }
        }
    }
//tag::implemented[]
    Try<Author> tryFindOrSaveWhere(Long id) {
        tryGet(id).recover {
            assert ErrorContext.wrap(it).code == ErrorCodes.NOT_FOUND
            new Author(name:'lalaal')
        }.flatMap {found ->
            trySave(found)
        }
    }

    Try<Author> trySave(Author author) {
        author.tryValidate()
                .flatMap {
                    handleValidationException {
                        save(author)
                    }
                }
    }

    Try<Author> tryUpdate(Long id, AuthorModel author) {
        tryGet(id).flatMap { toUpdate ->
            toUpdate.updateFrom(author)
            toUpdate.tryValidate().flatMap {
                handleValidationException {
                    save(toUpdate)
                }
            }
        }
    }
//end::implemented[]

    Try<Void> tryDelete(Long authorId) {
        tryGet(authorId).map {
            delete(authorId, [flush: true])
            null as Void
        }
    }
}
