package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Invoice} entity.
 */
public class InvoiceDTO implements Serializable {

    private Long id;

    @NotNull
    private String boltInvoice;

    @NotNull
    private Long sats;

    @NotNull
    private Boolean settled;

    private String paidByPubKey;

    @NotNull
    private LocalDate createdAt;

    private LocalDate settledAt;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBoltInvoice() {
        return boltInvoice;
    }

    public void setBoltInvoice(String boltInvoice) {
        this.boltInvoice = boltInvoice;
    }

    public Long getSats() {
        return sats;
    }

    public void setSats(Long sats) {
        this.sats = sats;
    }

    public Boolean getSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    public String getPaidByPubKey() {
        return paidByPubKey;
    }

    public void setPaidByPubKey(String paidByPubKey) {
        this.paidByPubKey = paidByPubKey;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(LocalDate settledAt) {
        this.settledAt = settledAt;
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
        if (!(o instanceof InvoiceDTO)) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id=" + getId() +
            ", boltInvoice='" + getBoltInvoice() + "'" +
            ", sats=" + getSats() +
            ", settled='" + getSettled() + "'" +
            ", paidByPubKey='" + getPaidByPubKey() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", settledAt='" + getSettledAt() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
