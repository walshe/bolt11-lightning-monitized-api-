package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.Method;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A MonetizedApi.
 */
@Entity
@Table(name = "monetized_api")
public class MonetizedApi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    private Method method;

    @NotNull
    @Column(name = "uri", nullable = false)
    private String uri;

    @NotNull
    @Column(name = "price_in_sats", nullable = false)
    private Long priceInSats;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MonetizedApi id(Long id) {
        this.id = id;
        return this;
    }

    public Method getMethod() {
        return this.method;
    }

    public MonetizedApi method(Method method) {
        this.method = method;
        return this;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getUri() {
        return this.uri;
    }

    public MonetizedApi uri(String uri) {
        this.uri = uri;
        return this;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long getPriceInSats() {
        return this.priceInSats;
    }

    public MonetizedApi priceInSats(Long priceInSats) {
        this.priceInSats = priceInSats;
        return this;
    }

    public void setPriceInSats(Long priceInSats) {
        this.priceInSats = priceInSats;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonetizedApi)) {
            return false;
        }
        return id != null && id.equals(((MonetizedApi) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonetizedApi{" +
            "id=" + getId() +
            ", method='" + getMethod() + "'" +
            ", uri='" + getUri() + "'" +
            ", priceInSats=" + getPriceInSats() +
            "}";
    }
}
