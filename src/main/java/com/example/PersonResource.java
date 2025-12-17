package com.example;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    @GET
    public List<Person> list() {
        return Person.listAll();
    }

    @GET
    @Path("/{id}")
    public Person get(@PathParam("id") Long id) {
        return Person.findById(id);
    }

    @POST
    @Transactional
    public Person create(Person person) {
        person.persist();
        return person;
    }
}