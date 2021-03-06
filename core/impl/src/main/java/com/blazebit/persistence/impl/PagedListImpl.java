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

import com.blazebit.persistence.KeySet;
import com.blazebit.persistence.PagedList;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @param <T> the type of elements in this list
 * @author Christian Beikov
 * @author Moritz Becker
 * @since 1.0
 */
public class PagedListImpl<T> extends ArrayList<T> implements PagedList<T> {

    private final KeySet keySet;
    private final long totalSize;

    public PagedListImpl(long totalSize) {
        this.keySet = null;
        this.totalSize = totalSize;
    }

    PagedListImpl(Collection<? extends T> collection, KeySet keySet, long totalSize) {
        super(collection);
        this.keySet = keySet;
        this.totalSize = totalSize;
    }

    @Override
    public long totalSize() {
        return totalSize;
    }

    @Override
    public KeySet getKeySet() {
        return keySet;
    }

}
