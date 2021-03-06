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

import com.blazebit.persistence.RestrictionBuilder;
import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.impl.expression.Expression;
import com.blazebit.persistence.impl.expression.ExpressionFactory;
import com.blazebit.persistence.impl.predicate.BetweenPredicate;
import com.blazebit.persistence.impl.predicate.EqPredicate;
import com.blazebit.persistence.impl.predicate.ExistsPredicate;
import com.blazebit.persistence.impl.predicate.GePredicate;
import com.blazebit.persistence.impl.predicate.GtPredicate;
import com.blazebit.persistence.impl.predicate.InPredicate;
import com.blazebit.persistence.impl.predicate.LePredicate;
import com.blazebit.persistence.impl.predicate.LikePredicate;
import com.blazebit.persistence.impl.predicate.LtPredicate;
import com.blazebit.persistence.impl.predicate.NotInPredicate;
import com.blazebit.persistence.impl.predicate.NotPredicate;
import com.blazebit.persistence.impl.predicate.Predicate;
import com.blazebit.persistence.impl.predicate.VisitorAdapter;

/**
 *
 * @author Moritz Becker
 * @since 1.0
 */
public abstract class PredicateManager<T> extends AbstractManager {

    protected final SubqueryInitiatorFactory subqueryInitFactory;
    final RootPredicate rootPredicate;
    private RightHandsideSubqueryPredicateBuilder<?> rightSubqueryPredicateBuilderListener;
    private final LeftHandsideSubqueryPredicateBuilder leftSubqueryPredicateBuilderListener = new LeftHandsideSubqueryPredicateBuilder();
    private SuperExpressionLeftHandsideSubqueryPredicateBuilder superExprLeftSubqueryPredicateBuilderListener;
    protected final ExpressionFactory expressionFactory;

    PredicateManager(QueryGenerator queryGenerator, ParameterManager parameterManager, SubqueryInitiatorFactory subqueryInitFactory, ExpressionFactory expressionFactory) {
        super(queryGenerator, parameterManager);
        this.rootPredicate = new RootPredicate(parameterManager);
        this.subqueryInitFactory = subqueryInitFactory;
        this.expressionFactory = expressionFactory;
    }

    RootPredicate getRootPredicate() {
        return rootPredicate;
    }

    RestrictionBuilder<T> restrict(AbstractBaseQueryBuilder<?, ?> builder, Expression expr) {
        return rootPredicate.startBuilder(new RestrictionBuilderImpl<T>((T) builder, rootPredicate, expr, subqueryInitFactory, expressionFactory, isAllowCaseWhenExpressions()));
    }

    SubqueryInitiator<RestrictionBuilder<T>> restrict(AbstractBaseQueryBuilder<?, ?> builder) {
        RestrictionBuilder<T> restrictionBuilder = (RestrictionBuilder<T>) rootPredicate.startBuilder(
            new RestrictionBuilderImpl<T>((T) builder, rootPredicate, subqueryInitFactory, expressionFactory, isAllowCaseWhenExpressions()));
        return subqueryInitFactory.createSubqueryInitiator(restrictionBuilder, leftSubqueryPredicateBuilderListener);
    }

    SubqueryInitiator<RestrictionBuilder<T>> restrict(AbstractBaseQueryBuilder<?, ?> builder, String subqueryAlias, String expression) {
        Expression expr = expressionFactory.createSimpleExpression(expression);
        superExprLeftSubqueryPredicateBuilderListener = new SuperExpressionLeftHandsideSubqueryPredicateBuilder(subqueryAlias, expr);
        RestrictionBuilder<T> restrictionBuilder = (RestrictionBuilder<T>) rootPredicate.startBuilder(
            new RestrictionBuilderImpl<T>((T) builder, rootPredicate, subqueryInitFactory, expressionFactory, isAllowCaseWhenExpressions()));
        return subqueryInitFactory.createSubqueryInitiator(restrictionBuilder, superExprLeftSubqueryPredicateBuilderListener);
    }

    SubqueryInitiator<T> restrictExists(T result) {
        rightSubqueryPredicateBuilderListener = rootPredicate.startBuilder(new RightHandsideSubqueryPredicateBuilder(
            rootPredicate, new ExistsPredicate()));
        return subqueryInitFactory.createSubqueryInitiator(result, rightSubqueryPredicateBuilderListener);
    }

    SubqueryInitiator<T> restrictNotExists(T result) {
        RightHandsideSubqueryPredicateBuilder<?> subqueryListener = rootPredicate.startBuilder(
            new RightHandsideSubqueryPredicateBuilder(rootPredicate, new NotPredicate(new ExistsPredicate())));
        return subqueryInitFactory.createSubqueryInitiator(result, subqueryListener);
    }

    void applyTransformer(ExpressionTransformer transformer) {
        // carry out transformations
        rootPredicate.predicate.accept(new TransformationVisitor(transformer));
    }

    void verifyBuilderEnded() {
        rootPredicate.verifyBuilderEnded();
        leftSubqueryPredicateBuilderListener.verifySubqueryBuilderEnded();
        if (rightSubqueryPredicateBuilderListener != null) {
            rightSubqueryPredicateBuilderListener.verifySubqueryBuilderEnded();
        }
        if (superExprLeftSubqueryPredicateBuilderListener != null) {
            superExprLeftSubqueryPredicateBuilderListener.verifySubqueryBuilderEnded();
        }
    }

    void acceptVisitor(Predicate.Visitor v) {
        rootPredicate.predicate.accept(v);
    }

    boolean hasPredicates() {
        return rootPredicate.predicate.getChildren().size() > 0;
    }

    void buildClause(StringBuilder sb) {
        queryGenerator.setQueryBuffer(sb);
        if (!hasPredicates()) {
            return;
        }
        sb.append(' ').append(getClauseName()).append(' ');
        applyPredicate(queryGenerator, sb);
    }

    void buildClausePredicate(StringBuilder sb) {
        queryGenerator.setQueryBuffer(sb);
        applyPredicate(queryGenerator, sb);
    }

    protected abstract String getClauseName();

    protected abstract boolean isAllowCaseWhenExpressions();

    void applyPredicate(QueryGenerator queryGenerator, StringBuilder sb) {
        rootPredicate.predicate.accept(queryGenerator);
    }

    static class TransformationVisitor extends VisitorAdapter {

        private final ExpressionTransformer transformer;

        public TransformationVisitor(ExpressionTransformer transformer) {
            this.transformer = transformer;
        }

        @Override
        public void visit(BetweenPredicate predicate) {
            predicate.setStart(transformer.transform(predicate.getStart()));
            predicate.setLeft(transformer.transform(predicate.getLeft()));
            predicate.setEnd(transformer.transform(predicate.getEnd()));
        }

        @Override
        public void visit(GePredicate predicate) {
            predicate.setLeft(transformer.transform(predicate.getLeft()));
            predicate.setRight(transformer.transform(predicate.getRight()));
        }

        @Override
        public void visit(GtPredicate predicate) {
            predicate.setLeft(transformer.transform(predicate.getLeft()));
            predicate.setRight(transformer.transform(predicate.getRight()));
        }

        @Override
        public void visit(LikePredicate predicate) {
            predicate.setLeft(transformer.transform(predicate.getLeft()));
            predicate.setRight(transformer.transform(predicate.getRight()));
        }

        @Override
        public void visit(EqPredicate predicate) {
            predicate.setLeft(transformer.transform(predicate.getLeft()));
            predicate.setRight(transformer.transform(predicate.getRight()));
        }

        @Override
        public void visit(LePredicate predicate) {
            predicate.setLeft(transformer.transform(predicate.getLeft()));
            predicate.setRight(transformer.transform(predicate.getRight()));
        }

        @Override
        public void visit(LtPredicate predicate) {
            predicate.setLeft(transformer.transform(predicate.getLeft()));
            predicate.setRight(transformer.transform(predicate.getRight()));
        }

        @Override
        public void visit(InPredicate predicate) {
            predicate.setLeft(transformer.transform(predicate.getLeft()));
            predicate.setRight(transformer.transform(predicate.getRight()));
        }

        @Override
        public void visit(NotInPredicate predicate) {
            predicate.setLeft(transformer.transform(predicate.getLeft()));
            predicate.setRight(transformer.transform(predicate.getRight()));
        }
    }
}
