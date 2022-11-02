package example.controller


import error.ErrorResource
import io.micronaut.http.HttpHeaderValues
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreRest
import spock.lang.Specification
import spock.lang.Subject

import static example.testing.Generators.anyInt
import static example.testing.JwtFixtures.makeClientAuthentication
import static io.micronaut.http.HttpStatus.NOT_FOUND

@MicronautTest(packages = ["example.entity"], rollback = true)
@Subject(AuthorController)
class AuthorControllerSpec extends Specification {
    @Inject
    JwtTokenGenerator jwtTokenGenerator

    @Inject
    Api api

    @IgnoreRest
    def "It fails to get an non-existing Author"() {
        given:
        def token = viewer()

        when:
        def author = api.getAuthor(badId, bearerAuth(token))

        then:
        def ex = thrown(HttpClientResponseException)
        ex.status == NOT_FOUND
        ex.getResponse().getBody(ErrorResource).map {
            assert it.message == "Author with id ${badId} not found."
            it
        }.isPresent()

        where:
        badId = anyInt()
    }

    /*
     * Helpers
     */

    private String viewer() {
        def optionalToken = jwtTokenGenerator.generateToken(makeClientAuthentication(roles: ["ROLE_VIEW"]), 1000)
        assert optionalToken.isPresent()
        optionalToken.get()
    }

    private String bearerAuth(String token) {
        "${HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER} ${token}"
    }
}
