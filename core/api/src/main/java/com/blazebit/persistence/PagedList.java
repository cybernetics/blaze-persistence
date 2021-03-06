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

import java.util.List;

/**
 * An extended version of a {@linkplain List} which also provides access to the total size of the list.
 *
 * @param <T> the type of elements in this list
 * @author Christian Beikov
 * @since 1.0
 */
public interface PagedList<T> extends List<T> {

    /**
     * The total size of the list.
     *
     * @return The total size
     */
    public long totalSize();

    /**
     * Returns the key set for this paged list which can be used for key set pagination.
     * The key set may be null if key set pagination wasn't used.
     *
     * @see QueryBuilder#page(com.blazebit.persistence.KeySet, int, int)
     * @return
     */
    public KeySet getKeySet();
}
