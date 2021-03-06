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
package com.blazebit.persistence.view.impl;

import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Christian Beikov
 * @since 1.0
 */
public class EntityViewConfigurationImpl implements EntityViewConfiguration {

    private final Set<Class<?>> entityViewClasses = new HashSet<Class<?>>();

    @Override
    public void addEntityView(Class<?> clazz) {
        entityViewClasses.add(clazz);
    }

    @Override
    public Set<Class<?>> getEntityViews() {
        return entityViewClasses;
    }

    @Override
    public EntityViewManager createEntityViewManager() {
        return new EntityViewManagerImpl(this);
    }
}
