package io.github.agbaroni.linuxday22.quarkus;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ACCOUNTS")
public class Account implements Serializable {
    private static final long serialVersionUID = 8151103517700L;

    @Column(name = "IBAN")
    @Getter
    @Id
    @Setter
    private String iban;

    @JoinColumn(name = "USER_")
    @JsonbTransient
    @Getter
    @ManyToOne
    @Setter
    private User user;

    @Column(name = "AMOUNT")
    @DecimalMin("0.0")
    @Getter
    @Setter
    private Double amount;

    @Column(columnDefinition = "integer default 0",
	    name = "VERSION")
    @JsonbTransient
    @Version
    private Integer version;
}
