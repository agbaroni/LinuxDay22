package io.github.agbaroni.linuxday22.quarkus;

import java.io.Serializable;
import java.util.Set;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USERS")
public class User implements Serializable {
    private static final long serialVersionUID = 613131173273L;

    @Column(name = "USERNAME")
    @Getter
    @Id
    @Setter
    private String username;

    @Column(name = "FIRST_NAME")
    @Getter
    @Setter
    private String firstName;

    @Column(name = "LAST_NAME")
    @Getter
    @Setter
    private String lastName;

    @Column(columnDefinition = "integer default 0",
	    name = "VERSION")
    @JsonbTransient
    @Version
    private Integer version;

    @Getter
    @JsonbTransient
    @OneToMany(mappedBy = "user")
    @Setter
    private Set<Account> accounts;
}
