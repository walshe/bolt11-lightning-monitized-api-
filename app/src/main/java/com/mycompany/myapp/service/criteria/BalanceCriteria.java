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
 * Criteria class for the {@link com.mycompany.myapp.domain.Balance} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.BalanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /balances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BalanceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter sats;

    private LocalDateFilter updatedAt;

    private LongFilter userId;

    public BalanceCriteria() {}

    public BalanceCriteria(BalanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sats = other.sats == null ? null : other.sats.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public BalanceCriteria copy() {
        return new BalanceCriteria(this);
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

    public LocalDateFilter getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new LocalDateFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateFilter updatedAt) {
        this.updatedAt = updatedAt;
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
        final BalanceCriteria that = (BalanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sats, that.sats) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sats, updatedAt, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BalanceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sats != null ? "sats=" + sats + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
