package example.testing

import example.entity.Author
import groovy.transform.CompileStatic

import static java.util.Collections.EMPTY_MAP

@CompileStatic
class AuthorFixtures {
    static final Author createAuthor(Map overrides = EMPTY_MAP) {
        makeAuthor(overrides).save(flush: true, failOnError: true)
    }
    static final Author makeAuthor(Map overrides = EMPTY_MAP) {
        def values = [
                name: Generators.anyString()
        ] + overrides
        new Author(name: values.name)
    }
}