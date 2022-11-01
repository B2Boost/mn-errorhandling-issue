package example.testing

import groovy.transform.CompileStatic
import io.micronaut.security.authentication.ClientAuthentication

import static Generators.anyString

@CompileStatic
class JwtFixtures {
    static ClientAuthentication makeClientAuthentication(Map overrides = Collections.emptyMap()) {
        def values = [
                username: anyString(),
                roles   : ["ROLE_${anyString()}"]
        ] + overrides
        new ClientAuthentication("foo", [
                sub  : values.username,
                roles: values.roles
        ])
    }

}
