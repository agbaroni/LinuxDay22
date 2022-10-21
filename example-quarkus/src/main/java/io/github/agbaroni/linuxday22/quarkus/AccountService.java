package io.github.agbaroni.linuxday22.quarkus;

import io.smallrye.mutiny.Uni;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny;

@ApplicationScoped
public class AccountService implements Serializable {
    private static final long serialVersionUID = 323942115231901L;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    public Uni<Account> find(String iban) {
	return sessionFactory.withTransaction(session -> {
		return session.find(Account.class, iban);
	    });
    }

    public Uni<Account> update(String iban, Account account) {
	return sessionFactory.withTransaction(session -> {
		return session.find(Account.class, iban)
		    .onItem()
		    .transform(acc -> {
			    if (account.getAmount() != null) {
				acc.setAmount(account.getAmount());
			    }

			    return acc;
			});
	    });
    }
}
