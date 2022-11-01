package example.entity


import error.ValidationSupport
import example.model.AuthorModel
import grails.gorm.annotation.Entity
import groovy.transform.ToString
import io.micronaut.core.annotation.Introspected
import org.grails.datastore.gorm.GormEntity

@Introspected
@Entity
@ToString(includes = ["name"], includePackage=false, includeNames = true)
class Author extends AuthorModel implements GormEntity<Author>, ValidationSupport {
    String name

    static constraints = {
        name nullable: false, unique: true, maxSize: 100
    }


    Author updateFrom(AuthorModel other) {
//This works too
//        setName(other.name)
//or this
        name = other.name
        markDirty()
        this
    }

    //https://github.com/grails/grails-core/issues/10847 (marked not a bug)
    //LMU: this does not mark the domain object dirty,
    //so the object is not persisted to the DB at the end of the transaction :(
    //Note that in the Hibernate session the object remains updated
    Author updateFromBuggy(AuthorModel other) {
        this.name = other.name
        this
    }
}
