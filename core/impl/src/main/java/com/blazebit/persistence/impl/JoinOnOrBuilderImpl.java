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
package com.blazebit.persistence.impl;

import com.blazebit.persistence.JoinOnAndBuilder;
import com.blazebit.persistence.JoinOnOrBuilder;
import com.blazebit.persistence.RestrictionBuilder;
import com.blazebit.persistence.impl.expression.Expression;
import com.blazebit.persistence.impl.expression.ExpressionFactory;
import com.blazebit.persistence.impl.predicate.OrPredicate;
import com.blazebit.persistence.impl.predicate.Predicate;
import com.blazebit.persistence.impl.predicate.PredicateBuilder;
import com.blazebit.persistence.impl.predicate.PredicateBuilderEndedListener;

/**
 *
 * @author Moritz Becker
 * @since 1.0
 */
public class JoinOnOrBuilderImpl<T> extends PredicateAndSubqueryBuilderEndedListener<T> implements JoinOnOrBuilder<T>,
    PredicateBuilder {

    private final T result;
    private final PredicateBuilderEndedListener listener;
    private final OrPredicate predicate = new OrPredicate();
    private final ExpressionFactory expressionFactory;
    private final SubqueryInitiatorFactory subqueryInitFactory;

    public JoinOnOrBuilderImpl(T result, PredicateBuilderEndedListener listener, ExpressionFactory expressionFactory, SubqueryInitiatorFactory subqueryInitFactory) {
        this.result = result;
        this.listener = listener;
        this.expressionFactory = expressionFactory;
        this.subqueryInitFactory = subqueryInitFactory;
    }

    @Override
    public T endOr() {
        verifyBuilderEnded();
        listener.onBuilderEnded(this);
        return result;
    }

    @Override
    public void onBuilderEnded(PredicateBuilder builder) {
        super.onBuilderEnded(builder);
        predicate.getChildren().add(builder.getPredicate());
    }

    @Override
    public JoinOnAndBuilder<JoinOnOrBuilder<T>> onAnd() {
        return startBuilder(new JoinOnAndBuilderImpl<JoinOnOrBuilder<T>>(this, this, expressionFactory, subqueryInitFactory));
    }

    @Override
    public RestrictionBuilder<JoinOnOrBuilder<T>> on(String expression) {
        Expression leftExpression = expressionFactory.createSimpleExpression(expression);
        return startBuilder(new RestrictionBuilderImpl<JoinOnOrBuilder<T>>(this, this, leftExpression, subqueryInitFactory, expressionFactory, false));
    }

    @Override
    public Predicate getPredicate() {
        return predicate;
    }

}
