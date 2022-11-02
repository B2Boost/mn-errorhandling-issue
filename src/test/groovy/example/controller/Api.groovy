package example.controller

import error.ErrorResource
import example.resource.AuthorResource
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client

@Client(value = "/", errorType = ErrorResource)
interface Api {
    @Get("/author/{id}")
    AuthorResource getAuthor(Long id, @Header(name="Authorization") String authorization)
}