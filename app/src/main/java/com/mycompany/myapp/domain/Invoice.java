package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bolt_invoice", nullable = false)
    private String boltInvoice;

    @NotNull
    @Column(name = "sats", nullable = false)
    private Long sats;

    @NotNull
    @Column(name = "settled", nullable = false)
    private Boolean settled;

    @Column(name = "paid_by_pub_key")
    private String paidByPubKey;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "settled_at")
    private LocalDate settledAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice id(Long id) {
        this.id = id;
        return this;
    }

    public String getBoltInvoice() {
        return this.boltInvoice;
    }

    public Invoice boltInvoice(String boltInvoice) {
        this.boltInvoice = boltInvoice;
        return this;
    }

    public void setBoltInvoice(String boltInvoice) {
        this.boltInvoice = boltInvoice;
    }

    public Long getSats() {
        return this.sats;
    }

    public Invoice sats(Long sats) {
        this.sats = sats;
        return this;
    }

    public void setSats(Long sats) {
        this.sats = sats;
    }

    public Boolean getSettled() {
        return this.settled;
    }

    public Invoice settled(Boolean settled) {
        this.settled = settled;
        return this;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    public String getPaidByPubKey() {
        return this.paidByPubKey;
    }

    public Invoice paidByPubKey(String paidByPubKey) {
        this.paidByPubKey = paidByPubKey;
        return this;
    }

    public void setPaidByPubKey(String paidByPubKey) {
        this.paidByPubKey = paidByPubKey;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Invoice createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getSettledAt() {
        return this.settledAt;
    }

    public Invoice settledAt(LocalDate settledAt) {
        this.settledAt = settledAt;
        return this;
    }

    public void setSettledAt(LocalDate settledAt) {
        this.settledAt = settledAt;
    }

    public User getUser() {
        return this.user;
    }

    public Invoice user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", boltInvoice='" + getBoltInvoice() + "'" +
            ", sats=" + getSats() +
            ", settled='" + getSettled() + "'" +
            ", paidByPubKey='" + getPaidByPubKey() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", settledAt='" + getSettledAt() + "'" +
            "}";
    }
}
