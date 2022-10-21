package io.github.agbaroni.linuxday22.quarkus;

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
@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource implements Serializable {
    private static final long serialVersionUID = 74265867475L;

    @Inject
    AccountService accountService;

    @Inject
    Validator validator;

    @GET
    @Path("/{iban}")
    @PermitAll
    public Uni<Account> find(@PathParam("iban") String iban) {
	return accountService.find(iban);
    }

    @PUT
    @Path("/{iban}")
    @RolesAllowed({ "operator" })
    public Uni<Account> update(@PathParam("iban") String iban, Account account) {
	var violations = validator.validate(account, Account.class);

	if (violations.isEmpty()) {
	    return accountService.update(iban, account);
	}

	return Uni.createFrom().nothing();
    }
}
