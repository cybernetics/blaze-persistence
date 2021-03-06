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
package com.blazebit.persistence.view.filter;

import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.AttributeFilterProvider;
import com.blazebit.persistence.view.AttributeFilter;

/**
 * A placeholder for a filter implementation that implements a contains filter.
 * This placeholder can be used in a {@link AttributeFilter} annotation or you can retrieve an instance of this filter by invoking
 * {@link EntityViewManager#createFilter(java.lang.Class, java.lang.Class, java.lang.Object)}.
 *
 * A contains filter accepts an object. The {@linkplain Object#toString()} representation of that object will be used as value
 * for the contains restriction.
 *
 * @author Christian Beikov
 * @since 1.0
 */
public abstract class ContainsFilter extends AttributeFilterProvider {
}
