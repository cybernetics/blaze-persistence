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

import com.blazebit.persistence.SubqueryInitiator;
import javax.persistence.EntityManager;

/**
 *
 * @author ccbem
 */
public class SubqueryInitiatorFactory {
	private final CriteriaBuilderFactoryImpl cbf;
    private final EntityManager em;
    private final ParameterManager parameterManager;
    private final AliasManager aliasManager;

    public SubqueryInitiatorFactory(CriteriaBuilderFactoryImpl cbf, EntityManager em, ParameterManager parameterManager, AliasManager aliasManager) {
		this.cbf = cbf;
        this.em = em;
        this.parameterManager = parameterManager;
        this.aliasManager = aliasManager;
    }
    
    public <T> SubqueryInitiator<T> createSubqueryInitiator(T result, SubqueryBuilderListener listener){
        return new SubqueryInitiatorImpl<T>(cbf, em, result, parameterManager, aliasManager, listener);
    }
}
