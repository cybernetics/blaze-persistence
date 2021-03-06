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

import com.blazebit.persistence.impl.predicate.PredicateBuilder;
import com.blazebit.persistence.impl.predicate.PredicateBuilderEndedListener;

/**
 *
 * @author Moritz Becker
 * @since 1.0
 */
public class PredicateAndSubqueryBuilderEndedListener<T> implements PredicateBuilderEndedListener, SubqueryBuilderListener<T> {

    private final PredicateBuilderEndedListenerImpl predicateBuilderListener = new PredicateBuilderEndedListenerImpl();
    private final SubqueryBuilderListenerImpl<T> subqueryBuilderListener = new SubqueryBuilderListenerImpl<T>();

    @Override
    public void onBuilderEnded(PredicateBuilder o) {
        predicateBuilderListener.onBuilderEnded(o);
    }

    @Override
    public void onBuilderEnded(SubqueryBuilderImpl<T> builder) {
        subqueryBuilderListener.onBuilderEnded(builder);
    }

    @Override
    public void onBuilderStarted(SubqueryBuilderImpl<T> builder) {
        subqueryBuilderListener.onBuilderStarted(builder);
    }

    protected void verifyBuilderEnded() {
        predicateBuilderListener.verifyBuilderEnded();
        subqueryBuilderListener.verifySubqueryBuilderEnded();
    }

    protected <T extends PredicateBuilder> T startBuilder(T builder) {
        return predicateBuilderListener.startBuilder(builder);
    }
}
