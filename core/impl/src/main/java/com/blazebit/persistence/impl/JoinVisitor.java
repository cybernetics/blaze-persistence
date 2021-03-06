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

import com.blazebit.persistence.impl.expression.OuterExpression;
import com.blazebit.persistence.impl.expression.PathExpression;
import com.blazebit.persistence.impl.predicate.VisitorAdapter;

/**
 *
 * @author Moritz Becker
 * @since 1.0
 */
public class JoinVisitor extends VisitorAdapter {

    private final JoinManager joinManager;
    private boolean joinWithObjectLeafAllowed = true;
    private boolean fromSelect = false;

    public JoinVisitor(JoinManager joinManager) {
        this.joinManager = joinManager;
    }

    public boolean isFromSelect() {
        return fromSelect;
    }

    public void setFromSelect(boolean fromSelect) {
        this.fromSelect = fromSelect;
    }

    @Override
    public void visit(PathExpression expression) {
        joinManager.implicitJoin(expression, joinWithObjectLeafAllowed, fromSelect, false, false);
    }

    @Override
    public void visit(OuterExpression expression) {
        // do not join outer expressions
    }

    public boolean isJoinWithObjectLeafAllowed() {
        return joinWithObjectLeafAllowed;
    }

    public void setJoinWithObjectLeafAllowed(boolean joinWithObjectLeafAllowed) {
        this.joinWithObjectLeafAllowed = joinWithObjectLeafAllowed;
    }
}
