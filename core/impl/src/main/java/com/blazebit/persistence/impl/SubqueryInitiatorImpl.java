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

import com.blazebit.persistence.SubqueryBuilder;
import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.impl.expression.ExpressionFactory;
import javax.persistence.EntityManager;

/**
 *
 * @author Moritz Becker
 * @since 1.0
 */
public class SubqueryInitiatorImpl<X> implements SubqueryInitiator<X> {

    private final CriteriaBuilderFactoryImpl cbf;
    private final EntityManager em;
    private final X result;
    private final ParameterManager parameterManager;
    private final AliasManager aliasManager;
    private final SubqueryBuilderListener listener;
    private final ExpressionFactory expressionFactory;
    private final JoinManager parentJoinManager;
    private final ArrayExpressionTransformer parentArrayExpressionTransformer;

    public SubqueryInitiatorImpl(CriteriaBuilderFactoryImpl cbf, EntityManager em, X result, ParameterManager parameterManager, AliasManager aliasManager, JoinManager parentJoinManager, SubqueryBuilderListener listener, ExpressionFactory expressionFactory, ArrayExpressionTransformer parentArrayExpressionTransformer) {
        this.cbf = cbf;
        this.em = em;
        this.result = result;
        this.parameterManager = parameterManager;
        this.aliasManager = aliasManager;
        this.listener = listener;
        this.expressionFactory = expressionFactory;
        this.parentJoinManager = parentJoinManager;
        this.parentArrayExpressionTransformer = parentArrayExpressionTransformer;
    }

    @Override
    public SubqueryBuilder<X> from(Class<?> clazz) {
        SubqueryBuilderImpl<X> subqueryBuilder = new SubqueryBuilderImpl<X>(cbf, em, clazz, null, result, parameterManager, aliasManager, parentJoinManager, listener,
                                                                            expressionFactory, parentArrayExpressionTransformer);
        listener.onBuilderStarted(subqueryBuilder);
        return subqueryBuilder;
    }

    @Override
    public SubqueryBuilder<X> from(Class<?> clazz, String alias) {
        SubqueryBuilderImpl<X> subqueryBuilder = new SubqueryBuilderImpl<X>(cbf, em, clazz, alias, result, parameterManager, aliasManager, parentJoinManager, listener,
                                                                            expressionFactory, parentArrayExpressionTransformer);
        listener.onBuilderStarted(subqueryBuilder);
        return subqueryBuilder;
    }

}
