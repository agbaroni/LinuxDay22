package io.github.agbaroni.linuxday22.quarkus;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.Setter;

// @Embeddable
// @EqualsAndHashCode
public class AccountKey implements Serializable {
    private static final long serialVersionUID = 208871765011484L;

    // @Column(name = "IBAN")
    // @Getter
    // @Setter
    private String iban;

    // @JoinColumn(name = "USER_")
    // @Getter
    // @ManyToOne
    // @Setter
    private User user;
}
