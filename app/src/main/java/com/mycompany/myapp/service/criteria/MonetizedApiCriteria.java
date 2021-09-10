package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.Method;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.MonetizedApi} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MonetizedApiResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /monetized-apis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MonetizedApiCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Method
     */
    public static class MethodFilter extends Filter<Method> {

        public MethodFilter() {}

        public MethodFilter(MethodFilter filter) {
            super(filter);
        }

        @Override
        public MethodFilter copy() {
            return new MethodFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private MethodFilter method;

    private StringFilter uri;

    private LongFilter priceInSats;

    public MonetizedApiCriteria() {}

    public MonetizedApiCriteria(MonetizedApiCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.method = other.method == null ? null : other.method.copy();
        this.uri = other.uri == null ? null : other.uri.copy();
        this.priceInSats = other.priceInSats == null ? null : other.priceInSats.copy();
    }

    @Override
    public MonetizedApiCriteria copy() {
        return new MonetizedApiCriteria(this);
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

    public MethodFilter getMethod() {
        return method;
    }

    public MethodFilter method() {
        if (method == null) {
            method = new MethodFilter();
        }
        return method;
    }

    public void setMethod(MethodFilter method) {
        this.method = method;
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

    public LongFilter getPriceInSats() {
        return priceInSats;
    }

    public LongFilter priceInSats() {
        if (priceInSats == null) {
            priceInSats = new LongFilter();
        }
        return priceInSats;
    }

    public void setPriceInSats(LongFilter priceInSats) {
        this.priceInSats = priceInSats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MonetizedApiCriteria that = (MonetizedApiCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(method, that.method) &&
            Objects.equals(uri, that.uri) &&
            Objects.equals(priceInSats, that.priceInSats)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, method, uri, priceInSats);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonetizedApiCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (method != null ? "method=" + method + ", " : "") +
            (uri != null ? "uri=" + uri + ", " : "") +
            (priceInSats != null ? "priceInSats=" + priceInSats + ", " : "") +
            "}";
    }
}
