package io.github.agbaroni.linuxday22.quarkus;

import io.smallrye.mutiny.Uni;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny;

@ApplicationScoped
public class UserService implements Serializable {
    private static final long serialVersionUID = 323942115231901L;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    public Uni<User> find(String username) {
	return sessionFactory.withTransaction(session -> {
		return session.find(User.class, username);
	    });
    }

    public Uni<User> update(String username, User user) {
	return sessionFactory.withTransaction(session -> {
		return session.find(User.class, username)
		    .onItem()
		    .transform(usr -> {
			    if (usr.getFirstName() == null) {
				usr.setFirstName(usr.getFirstName());
			    }

			    if (usr.getLastName() == null) {
				usr.setLastName(usr.getLastName());
			    }

			    return usr;
			});
	    });
    }
}
