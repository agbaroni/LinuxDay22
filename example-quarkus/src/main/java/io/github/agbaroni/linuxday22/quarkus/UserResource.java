package io.github.agbaroni.linuxday22.quarkus;

// import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;

import java.io.Serializable;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource implements Serializable {
    private static final long serialVersionUID = 141372813312100L;

    // @Inject
    // SecurityIdentity securityIdentity;

    @Inject
    UserService userService;

    @Inject
    Validator validator;

    @GET
    @Path("/{username}")
    @PermitAll
    public Uni<User> find(@PathParam("username") String username) {
	return userService.find(username);
    }

    @PUT
    @Path("/{username}")
    @RolesAllowed({ "operator" })
    public Uni<User> update(@PathParam("username") String username, User user) {
	var violations = validator.validate(user, User.class);

	if (violations.isEmpty()) {
	    return userService.update(username, user);
	}

	return Uni.createFrom().nothing();
    }
}
