/*
 * Copyright 2014 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.persistence;

import javax.persistence.Tuple;

/**
 * A builder for paginated criteria queries.
 *
 * @param <T> The query result type
 * @author Christian Beikov
 * @since 1.0
 */
public interface PaginatedCriteriaBuilder<T> extends QueryBuilder<T, PaginatedCriteriaBuilder<T>> {

    /**
     * Returns the query string that selects the count of elements.
     *
     * @return The query string
     */
    public String getPageCountQueryString();

    /**
     * Returns the query string that selects the id of the elements.
     *
     * @return The query string
     */
    public String getPageIdQueryString();

    /*
     * Covariant overrides
     */
    @Override
    public PagedList<T> getResultList();

    @Override
    public SimpleCaseWhenBuilder<PaginatedCriteriaBuilder<Tuple>> selectSimpleCase(String expression);

    @Override
    public SimpleCaseWhenBuilder<PaginatedCriteriaBuilder<Tuple>> selectSimpleCase(String expression, String alias);

    @Override
    public CaseWhenBuilder<PaginatedCriteriaBuilder<Tuple>> selectCase();

    @Override
    public CaseWhenBuilder<PaginatedCriteriaBuilder<Tuple>> selectCase(String alias);

    @Override
    public <Y> SelectObjectBuilder<PaginatedCriteriaBuilder<Y>> selectNew(Class<Y> clazz);

    @Override
    public <Y> PaginatedCriteriaBuilder<Y> selectNew(ObjectBuilder<Y> builder);

    @Override
    public PaginatedCriteriaBuilder<Tuple> select(String expression);

    @Override
    public PaginatedCriteriaBuilder<Tuple> select(String expression, String alias);

    @Override
    public SubqueryInitiator<PaginatedCriteriaBuilder<Tuple>> selectSubquery();

    @Override
    public SubqueryInitiator<PaginatedCriteriaBuilder<Tuple>> selectSubquery(String alias);

    @Override
    public SubqueryInitiator<PaginatedCriteriaBuilder<Tuple>> selectSubquery(String subqueryAlias, String expression, String selectAlias);

    @Override
    public SubqueryInitiator<PaginatedCriteriaBuilder<Tuple>> selectSubquery(String subqueryAlias, String expression);

}
