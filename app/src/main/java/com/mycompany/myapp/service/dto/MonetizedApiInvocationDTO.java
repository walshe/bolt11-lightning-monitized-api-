package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.MonetizedApiInvocation} entity.
 */
public class MonetizedApiInvocationDTO implements Serializable {

    private Long id;

    @NotNull
    private String uri;

    @NotNull
    private LocalDate createdAt;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonetizedApiInvocationDTO)) {
            return false;
        }

        MonetizedApiInvocationDTO monetizedApiInvocationDTO = (MonetizedApiInvocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, monetizedApiInvocationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonetizedApiInvocationDTO{" +
            "id=" + getId() +
            ", uri='" + getUri() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
