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

import com.blazebit.persistence.BaseQueryBuilder;
import com.blazebit.persistence.impl.expression.ArrayExpression;
import com.blazebit.persistence.impl.expression.CompositeExpression;
import com.blazebit.persistence.impl.expression.Expression;
import com.blazebit.persistence.impl.expression.FooExpression;
import com.blazebit.persistence.impl.expression.PathElementExpression;
import com.blazebit.persistence.impl.expression.PathExpression;
import com.blazebit.persistence.impl.predicate.AndPredicate;
import com.blazebit.persistence.impl.predicate.EqPredicate;
import com.blazebit.persistence.impl.predicate.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @deprecated This transformer is actually not used any more because the transformations are included in the join management code
 * @author Moritz Becker
 * @since 1.0
 */
@Deprecated
public class ArrayExpressionTransformer implements ExpressionTransformer {

    private final Map<TransformationInfo, EqPredicate> transformedPathFilterMap = new HashMap<TransformationInfo, EqPredicate>();
    private final JoinManager joinManager;
    private final BaseQueryBuilder<?, ?> aliasOwner;
    private final ArrayExpressionTransformer parentTransformer;

    public ArrayExpressionTransformer(JoinManager joinManager, BaseQueryBuilder<?, ?> aliasOwner, ArrayExpressionTransformer parentArrayExpressionTransformer) {
        this.joinManager = joinManager;
        this.aliasOwner = aliasOwner;
        this.parentTransformer = parentArrayExpressionTransformer;
    }

    @Override
    public Expression transform(Expression original) {
        if (original instanceof CompositeExpression) {
            CompositeExpression composite = (CompositeExpression) original;
            CompositeExpression transformed = new CompositeExpression(new ArrayList<Expression>());
            for (Expression e : composite.getExpressions()) {
                transformed.getExpressions().add(transform(e));
            }
            return transformed;
        }

        if (!(original instanceof PathExpression)) {
            return original;
        }

        PathExpression path = (PathExpression) original;
        ArrayExpression arrayExp;

        String absBasePath;
        int loopEndIndex = 0;
        if (path.getBaseNode() != null) {
            // do not transform external paths
            JoinNode baseNode = (JoinNode) path.getBaseNode();
            if (baseNode.getAliasInfo().getAliasOwner() != aliasOwner) {
                if (parentTransformer == null) {
                    throw new IllegalStateException("Could not transform array expression [" + path.toString() + "] with unknown alias owner");
                } else {
                    return parentTransformer.transform(original);
                }
            }

            absBasePath = baseNode.getAliasInfo().getAbsolutePath();

            if (path.getField() != null) {
                absBasePath += "." + path.getField();
            }

            if (path.getExpressions().get(0).toString().equals(joinManager.getRootAlias())) {
                loopEndIndex = 1;
            }
        } else {
            // this case is for single select and join aliases
            return original;
        }

        for (int i = path.getExpressions().size() - 1; i >= loopEndIndex; i--) {

            PathElementExpression expr = path.getExpressions().get(i);

            if (expr instanceof ArrayExpression) {
                arrayExp = (ArrayExpression) expr;

                String currentAbsPath = absBasePath;
                TransformationInfo transInfo = new TransformationInfo(currentAbsPath, arrayExp.getIndex().toString());

                if (transformedPathFilterMap.get(transInfo) == null) {
                    CompositeExpression keyExpression = new CompositeExpression(new ArrayList<Expression>());
                    keyExpression.getExpressions().add(new FooExpression("KEY("));

                    PathExpression keyPath = new PathExpression(new ArrayList<PathElementExpression>(), true);
                    keyPath.getExpressions().add(arrayExp.getBase());
                    keyExpression.getExpressions().add(keyPath);
                    keyExpression.getExpressions().add(new FooExpression(")"));
                    EqPredicate valueKeyFilterPredicate = new EqPredicate(keyExpression, arrayExp.getIndex());

                    // TODO: rewrite if the transformer should ever be used again
//                    keyPath.setBaseNode(joinManager.findNode(keyPath.getPath()));
                    // set the generated predicate on the join node
                    JoinNode joinNode = null;//joinManager.findNode(absBasePath);
                    if (joinNode.getWithPredicate() != null) {
                        Predicate currentPred = joinNode.getWithPredicate();
                        if (currentPred instanceof AndPredicate) {
                            // we have to create a new predicate due to concurrent modification
                            AndPredicate withAndPredicate = new AndPredicate();
                            withAndPredicate.getChildren().addAll(((AndPredicate) currentPred).getChildren());
                            withAndPredicate.getChildren().add(valueKeyFilterPredicate);
                            joinNode.setWithPredicate(withAndPredicate);
                        } else {
                            AndPredicate withAndPredicate = new AndPredicate();
                            withAndPredicate.getChildren().add(currentPred);
                            withAndPredicate.getChildren().add(valueKeyFilterPredicate);
                            joinNode.setWithPredicate(withAndPredicate);
                        }
                    } else {
                        AndPredicate withAndPredicate = new AndPredicate();
                        withAndPredicate.getChildren().add(valueKeyFilterPredicate);
                        joinNode.setWithPredicate(withAndPredicate);
                    }
                    transformedPathFilterMap.put(transInfo, valueKeyFilterPredicate);

                }
            }

            if (i == loopEndIndex) {
                absBasePath = "";
            } else {
                absBasePath = absBasePath.substring(0, absBasePath.lastIndexOf('.'));
            }
        }

        return original;
    }

    @Override
    public Expression transform(Expression original, boolean selectClause) {
        return transform(original);
    }

    private static class TransformationInfo {

        public TransformationInfo(String absoluteFieldPath, String indexedField) {
            this.absoluteFieldPath = absoluteFieldPath;
            this.indexedField = indexedField;
        }

        private final String absoluteFieldPath;
        private final String indexedField;

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + (this.absoluteFieldPath != null ? this.absoluteFieldPath.hashCode() : 0);
            hash = 97 * hash + (this.indexedField != null ? this.indexedField.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TransformationInfo other = (TransformationInfo) obj;
            if ((this.absoluteFieldPath == null) ? (other.absoluteFieldPath != null) : !this.absoluteFieldPath.equals(
                other.absoluteFieldPath)) {
                return false;
            }
            if ((this.indexedField == null) ? (other.indexedField != null) : !this.indexedField.equals(other.indexedField)) {
                return false;
            }
            return true;
        }
    }
}
