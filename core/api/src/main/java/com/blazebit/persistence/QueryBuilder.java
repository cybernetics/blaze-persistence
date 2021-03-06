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

import java.lang.reflect.Constructor;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.Metamodel;

/**
 * A base interface for builders that support normal query functionality.
 * This interface is shared between the criteria builder and paginated criteria builder.
 *
 * @param <T> The query result type
 * @param <X> The concrete builder type
 * @author Christian Beikov
 * @since 1.0
 */
public interface QueryBuilder<T, X extends QueryBuilder<T, X>> extends BaseQueryBuilder<T, X> {

    /**
     * Returns the JPA typed query for the built query.
     * The returned query is already parameterized with all known parameters.
     *
     * @return The typed query for the built query
     */
    public TypedQuery<T> getQuery();

    /**
     * Sets the given value as the value for the parameter with the given name.
     *
     * @param name  The name of the parameter which should be set
     * @param value The value of the parameter that should be set
     * @return The query builder for chaining calls
     */
    public X setParameter(String name, Object value);

    /**
     * Sets the given {@link Calendar} value as the value for the parameter with the given name.
     *
     * @param name         The name of the parameter which should be set
     * @param value        The value of the parameter that should be set
     * @param temporalType The temporal type of the value
     * @return The query builder for chaining calls
     */
    public X setParameter(String name, Calendar value, TemporalType temporalType);

    /**
     * Sets the given {@link Date} value as the value for the parameter with the given name.
     *
     * @param name         The name of the parameter which should be set
     * @param value        The value of the parameter that should be set
     * @param temporalType The temporal type of the value
     * @return The query builder for chaining calls
     */
    public X setParameter(String name, Date value, TemporalType temporalType);

    /**
     * Returns true if a parameter with the given name is registered, otherwise false.
     *
     * @param name The name of the parameter that should be checked
     * @return True if the parameter is registered, otherwise false
     */
    public boolean containsParameter(String name);

    /**
     * Returns true if a parameter with the given name is registered and a value has been set, otherwise false.
     *
     * @param name The name of the parameter that should be checked
     * @return True if the parameter is registered and a value has been set, otherwise false
     */
    public boolean isParameterSet(String name);

    /**
     * Returns the parameter object representing the parameter with the given name if {@link QueryBuilder#containsParameter(java.lang.String) } returns true, otherwise null.
     *
     * @param name The name of the parameter that should be returned
     * @return The parameter object if the parameter is registered, otherwise null
     */
    public Parameter<?> getParameter(String name);

    /**
     * Returns a set of all registered parameters.
     *
     * @return The set of registered parameters
     */
    public Set<? extends Parameter<?>> getParameters();

    /**
     * Returns the set value for the parameter with the given name. If no value has been set, or the parameter does not exist, null is returned.
     *
     * @param name The name of the parameter for which the value should be returned
     * @return The value of the parameter or null if no value has been set or the parameter does not exist
     */
    public Object getParameterValue(String name);

    /**
     * Execute the query and return the result as a type List.
     *
     * @return The list of the results
     */
    public List<T> getResultList();
    
    /**
     * Execute the query expecting a single result.
     *
     * @return The single result
     */
    public T getSingleResult();

    /**
     * Returns the JPA {@link Metamodel} of the persistence unit which is used by this query builder.
     *
     * @return The JPA metamodel
     */
    public Metamodel getMetamodel();

    /**
     * Paginates the results of this query.
     *
     * Please note:
     * The pagination only works on entity level and NOT on row level. This means
     * that for queries which yield multiple result set rows per entity (i.e. rows with
     * the same entity id), the multiple rows are treated as 1 page entry during
     * the pagination process. Hence, the result size of such paginated queries
     * might be greater than the specified page size.
     *
     * An example for such queries would be a query that joins a collection:
     * SELECT d.id, contacts.name FROM Document d LEFT JOIN d.contacts contacts
     * If one Document has associated multiple contacts, the above query will produce
     * multiple result set rows for this document.
     *
     * Since the pagination works on entity id level, the results are implicitely
     * grouped by id and distinct. Therefore calling distinct() or groupBy() on a
     * PaginatedCriteriaBuilder is not allowed.
     *
     * @param firstResult The position of the first result to retrieve, numbered from 0
     * @param maxResults  The maximum number of results to retrieve
     * @return This query builder as paginated query builder
     */
    public PaginatedCriteriaBuilder<T> page(int firstResult, int maxResults);

    /**
     * Like {@link QueryBuilder#page(int, int)} but additionally uses key set pagination when possible.
     * Key set pagination is under the following circumstances possible
     * <ul>
     * <li>this query builder and the one with which the key set was obtained are equal</li>
     * <li>{@link KeySet#getMaxResults()} and <code>maxResults</code> evaluate to the same value</li>
     * <li>The absolute value of {@link KeySet#getFirstResults()}<code> - firstResult</code> is 0</li>
     * <li>The absolute value of {@link KeySet#getFirstResults()}<code> - firstResult</code> is equal to the value of <code>maxResults</code></li>
     * </ul>
     *
     * @param keySet      The key set from a previous result
     * @param firstResult The position of the first result to retrieve, numbered from 0
     * @param maxResults  The maximum number of results to retrieve
     * @return This query builder as paginated query builder
     * @see PagedList#getKeySet()
     */
    public PaginatedCriteriaBuilder<T> page(KeySet keySet, int firstResult, int maxResults);

    /*
     * Join methods
     */
    /**
     * Adds a join to the query, possibly specializing implicit joins, and giving the joined element an alias.
     * If fetch is set to true, a join fetch will be added.
     *
     * @param path  The path to join
     * @param alias The alias for the joined element
     * @param type  The join type
     * @param fetch True if a join fetch should be added
     * @return The query builder for chaining calls
     */
    public X join(String path, String alias, JoinType type, boolean fetch);

    /**
     * Adds a join to the query, possibly specializing implicit joins, and giving the joined element an alias.
     * The resulting join will be the default join meaning that expressions which use the absolute path will refer to this join.
     * If fetch is set to true, a join fetch will be added.
     *
     * @param path  The path to join
     * @param alias The alias for the joined element
     * @param type  The join type
     * @param fetch True if a join fetch should be added
     * @return The query builder for chaining calls
     */
    public X joinDefault(String path, String alias, JoinType type, boolean fetch);

    /**
     * Adds an implicit join fetch to the query.
     *
     * @param path The path to join fetch
     * @return The query builder for chaining calls
     */
    public X fetch(String path);

    /**
     * Like {@link QueryBuilder#join(java.lang.String, java.lang.String, com.blazebit.persistence.JoinType, boolean) } but with {@link JoinType#INNER} and fetch set to true.
     *
     * @param path  The path to join
     * @param alias The alias for the joined element
     * @return The query builder for chaining calls
     */
    public X innerJoinFetch(String path, String alias);

    /**
     * Like {@link QueryBuilder#joinDefault(java.lang.String, java.lang.String, com.blazebit.persistence.JoinType, boolean) } but with {@link JoinType#INNER} and fetch set to true.
     *
     * @param path  The path to join
     * @param alias The alias for the joined element
     * @return The query builder for chaining calls
     */
    public X innerJoinFetchDefault(String path, String alias);

    /**
     * Like {@link QueryBuilder#join(java.lang.String, java.lang.String, com.blazebit.persistence.JoinType, boolean) } but with {@link JoinType#LEFT} and fetch set to true.
     *
     * @param path  The path to join
     * @param alias The alias for the joined element
     * @return The query builder for chaining calls
     */
    public X leftJoinFetch(String path, String alias);

    /**
     * Like {@link QueryBuilder#joinDefault(java.lang.String, java.lang.String, com.blazebit.persistence.JoinType, boolean) } but with {@link JoinType#LEFT} and fetch set to true.
     *
     * @param path  The path to join
     * @param alias The alias for the joined element
     * @return The query builder for chaining calls
     */
    public X leftJoinFetchDefault(String path, String alias);

    /**
     * Like {@link QueryBuilder#join(java.lang.String, java.lang.String, com.blazebit.persistence.JoinType, boolean) } but with {@link JoinType#RIGHT} and fetch set to true.
     *
     * @param path  The path to join
     * @param alias The alias for the joined element
     * @return The query builder for chaining calls
     */
    public X rightJoinFetch(String path, String alias);

    /**
     * Like {@link QueryBuilder#joinDefault(java.lang.String, java.lang.String, com.blazebit.persistence.JoinType, boolean) } but with {@link JoinType#RIGHT} and fetch set to true.
     *
     * @param path  The path to join
     * @param alias The alias for the joined element
     * @return The query builder for chaining calls
     */
    public X rightJoinFetchDefault(String path, String alias);

    /*
     * Select methods
     */
    /**
     * Starts a {@link SelectObjectBuilder} for the given class. The types of the parameter arguments used in the {@link SelectObjectBuilder} must match a constructor of the given class.
     *
     * @param <Y>   The new query result type specified by the given class
     * @param clazz The class which should be used for the select new select clause
     * @return The select object builder for the given class
     */
    public <Y> SelectObjectBuilder<? extends QueryBuilder<Y, ?>> selectNew(Class<Y> clazz);

    /**
     * Starts a {@link SelectObjectBuilder} for the given constructor. The types of the parameter arguments used in the {@link SelectObjectBuilder} must match the given constructor.
     *
     * @param <Y>         The new query result type specified by the given class
     * @param constructor The constructor which should be used for the select new select clause
     * @return The select object builder for the given class
     */
    public <Y> SelectObjectBuilder<? extends QueryBuilder<Y, ?>> selectNew(Constructor<Y> constructor);

    /**
     * Applies the given object builder to this query. The object builder provides the select clauses and is used to transform the result set tuples.
     *
     * @param <Y>     The new query result type specified by the given class
     * @param builder The object builder which transforms the result set into objects of type {@linkplain Y}
     * @return The query builder for chaining calls
     */
    public <Y> QueryBuilder<Y, ?> selectNew(ObjectBuilder<Y> builder);

    /*
     * Covariant overrides
     */
    @Override
    public SimpleCaseWhenBuilder<? extends QueryBuilder<Tuple, ?>> selectSimpleCase(String expression);

    @Override
    public SimpleCaseWhenBuilder<? extends QueryBuilder<Tuple, ?>> selectSimpleCase(String expression, String alias);

    @Override
    public CaseWhenBuilder<? extends QueryBuilder<Tuple, ?>> selectCase();

    @Override
    public CaseWhenBuilder<? extends QueryBuilder<Tuple, ?>> selectCase(String alias);

    @Override
    public QueryBuilder<Tuple, ?> select(String expression);

    @Override
    public QueryBuilder<Tuple, ?> select(String expression, String alias);

    @Override
    public SubqueryInitiator<? extends QueryBuilder<Tuple, ?>> selectSubquery();

    @Override
    public SubqueryInitiator<? extends QueryBuilder<Tuple, ?>> selectSubquery(String alias);

    @Override
    public SubqueryInitiator<? extends QueryBuilder<Tuple, ?>> selectSubquery(String subqueryAlias, String expression, String selectAlias);

    @Override
    public SubqueryInitiator<? extends QueryBuilder<Tuple, ?>> selectSubquery(String subqueryAlias, String expression);
}
