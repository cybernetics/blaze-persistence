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

import com.blazebit.persistence.impl.expression.Expression;
import com.blazebit.persistence.impl.expression.SubqueryExpression;

/**
 *
 * @author Moritz Becker
 * @since 1.0
 */
public class SuperExpressionSubqueryBuilderListener<T> extends SubqueryBuilderListenerImpl<T> {

    private final String subqueryAlias;
    protected final Expression superExpression;

    public SuperExpressionSubqueryBuilderListener(String subqueryAlias, Expression superExpression) {
        this.subqueryAlias = subqueryAlias;
        this.superExpression = superExpression;
    }

    @Override
    public void onBuilderEnded(SubqueryBuilderImpl<T> builder) {
        super.onBuilderEnded(builder);
        ExpressionUtils.replaceSubexpression(superExpression, subqueryAlias, new SubqueryExpression(builder));
    }

}
