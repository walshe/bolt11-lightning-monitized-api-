package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.Method;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.MonetizedApi} entity.
 */
public class MonetizedApiDTO implements Serializable {

    private Long id;

    @NotNull
    private Method method;

    @NotNull
    private String uri;

    @NotNull
    private Long priceInSats;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long getPriceInSats() {
        return priceInSats;
    }

    public void setPriceInSats(Long priceInSats) {
        this.priceInSats = priceInSats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonetizedApiDTO)) {
            return false;
        }

        MonetizedApiDTO monetizedApiDTO = (MonetizedApiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, monetizedApiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonetizedApiDTO{" +
            "id=" + getId() +
            ", method='" + getMethod() + "'" +
            ", uri='" + getUri() + "'" +
            ", priceInSats=" + getPriceInSats() +
            "}";
    }
}
