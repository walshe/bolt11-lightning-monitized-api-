package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Invoice} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.InvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InvoiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter boltInvoice;

    private LongFilter sats;

    private BooleanFilter settled;

    private StringFilter paidByPubKey;

    private LocalDateFilter createdAt;

    private LocalDateFilter settledAt;

    private LongFilter userId;

    public InvoiceCriteria() {}

    public InvoiceCriteria(InvoiceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.boltInvoice = other.boltInvoice == null ? null : other.boltInvoice.copy();
        this.sats = other.sats == null ? null : other.sats.copy();
        this.settled = other.settled == null ? null : other.settled.copy();
        this.paidByPubKey = other.paidByPubKey == null ? null : other.paidByPubKey.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.settledAt = other.settledAt == null ? null : other.settledAt.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public InvoiceCriteria copy() {
        return new InvoiceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBoltInvoice() {
        return boltInvoice;
    }

    public StringFilter boltInvoice() {
        if (boltInvoice == null) {
            boltInvoice = new StringFilter();
        }
        return boltInvoice;
    }

    public void setBoltInvoice(StringFilter boltInvoice) {
        this.boltInvoice = boltInvoice;
    }

    public LongFilter getSats() {
        return sats;
    }

    public LongFilter sats() {
        if (sats == null) {
            sats = new LongFilter();
        }
        return sats;
    }

    public void setSats(LongFilter sats) {
        this.sats = sats;
    }

    public BooleanFilter getSettled() {
        return settled;
    }

    public BooleanFilter settled() {
        if (settled == null) {
            settled = new BooleanFilter();
        }
        return settled;
    }

    public void setSettled(BooleanFilter settled) {
        this.settled = settled;
    }

    public StringFilter getPaidByPubKey() {
        return paidByPubKey;
    }

    public StringFilter paidByPubKey() {
        if (paidByPubKey == null) {
            paidByPubKey = new StringFilter();
        }
        return paidByPubKey;
    }

    public void setPaidByPubKey(StringFilter paidByPubKey) {
        this.paidByPubKey = paidByPubKey;
    }

    public LocalDateFilter getCreatedAt() {
        return createdAt;
    }

    public LocalDateFilter createdAt() {
        if (createdAt == null) {
            createdAt = new LocalDateFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(LocalDateFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateFilter getSettledAt() {
        return settledAt;
    }

    public LocalDateFilter settledAt() {
        if (settledAt == null) {
            settledAt = new LocalDateFilter();
        }
        return settledAt;
    }

    public void setSettledAt(LocalDateFilter settledAt) {
        this.settledAt = settledAt;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InvoiceCriteria that = (InvoiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(boltInvoice, that.boltInvoice) &&
            Objects.equals(sats, that.sats) &&
            Objects.equals(settled, that.settled) &&
            Objects.equals(paidByPubKey, that.paidByPubKey) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(settledAt, that.settledAt) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, boltInvoice, sats, settled, paidByPubKey, createdAt, settledAt, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (boltInvoice != null ? "boltInvoice=" + boltInvoice + ", " : "") +
            (sats != null ? "sats=" + sats + ", " : "") +
            (settled != null ? "settled=" + settled + ", " : "") +
            (paidByPubKey != null ? "paidByPubKey=" + paidByPubKey + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (settledAt != null ? "settledAt=" + settledAt + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
