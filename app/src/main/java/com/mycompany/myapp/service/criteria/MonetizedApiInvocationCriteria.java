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
 * Criteria class for the {@link com.mycompany.myapp.domain.MonetizedApiInvocation} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MonetizedApiInvocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /monetized-api-invocations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MonetizedApiInvocationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter uri;

    private LocalDateFilter createdAt;

    private LongFilter userId;

    public MonetizedApiInvocationCriteria() {}

    public MonetizedApiInvocationCriteria(MonetizedApiInvocationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uri = other.uri == null ? null : other.uri.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public MonetizedApiInvocationCriteria copy() {
        return new MonetizedApiInvocationCriteria(this);
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

    public StringFilter getUri() {
        return uri;
    }

    public StringFilter uri() {
        if (uri == null) {
            uri = new StringFilter();
        }
        return uri;
    }

    public void setUri(StringFilter uri) {
        this.uri = uri;
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
        final MonetizedApiInvocationCriteria that = (MonetizedApiInvocationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uri, that.uri) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uri, createdAt, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonetizedApiInvocationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uri != null ? "uri=" + uri + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
