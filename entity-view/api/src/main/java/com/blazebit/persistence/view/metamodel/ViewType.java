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
package com.blazebit.persistence.view.metamodel;

import java.util.Set;

/**
 * Represents the metamodel of an entity view.
 *
 * @param <X> The type of the entity view
 * @author Christian Beikov
 * @since 1.0
 */
public interface ViewType<X> {

    /**
     * Returns the name of the entity view.
     *
     * @return The name of the entity view.
     */
    public String getName();

    /**
     * Returns the java type of the entity view.
     *
     * @return The java type of the entity view
     */
    public Class<X> getJavaType();

    /**
     * Returns the entity class that the entity view uses.
     *
     * @return The entity class that the entity view uses
     */
    public Class<?> getEntityClass();

    /**
     * Returns the attributes of the entity view.
     *
     * @return The attributes of the entity view
     */
    public Set<MethodAttribute<? super X, ?>> getAttributes();

    /**
     * Returns the attribute of the entity view specified by the given name.
     *
     * @param name The name of the attribute which should be returned
     * @return The attribute of the entity view with the given name
     */
    public MethodAttribute<? super X, ?> getAttribute(String name);

    /**
     * Returns the mapping constructors of the entity view.
     *
     * @return The mapping constructors of the entity view
     */
    public Set<MappingConstructor<X>> getConstructors();

    /**
     * Returns the mapping constructor of the entity view specified by the given parameter types.
     *
     * @param parameterTypes The parameter types of the constructor which should be returned.
     * @return The mapping constructor of the entity view with the given parameter types
     */
    public MappingConstructor<X> getConstructor(Class<?>... parameterTypes);

    /**
     * Returns the names of the constructors of the entity view.
     *
     * @return The names of the constructors of the entity view
     */
    public Set<String> getConstructorNames();

    /**
     * Returns the constructor of the entity view specified by the given name.
     *
     * @param name The name of the constructor which should be returned
     * @return The constructor of the entity view with the given name
     */
    public MappingConstructor<X> getConstructor(String name);
    
    /**
     * Returns the filter mapping of the entity view with the given name.
     * 
     * @param filterName The name of the filter mapping which should be returned
     * @return The filter mapping of the entity view with the given name
     */
    public FilterMapping<?> getFilter(String filterName);
    
    /**
     * Returns the filter mappings of the entity view.
     * 
     * @return The filter mappings of the entity view
     */
    public Set<FilterMapping<?>> getFilters();
    
    /**
     * Returns the attribute filter mapping of the entity view with the given name.
     * 
     * @param filterName The name of the attribute filter mapping which should be returned
     * @return The attribute filter mapping of the entity view with the given name
     */
    public AttributeFilterMapping getAttributeFilter(String filterName);
    
    /**
     * Returns the attribute filter mappings of the entity view.
     * 
     * @return The attribute filter mappings of the entity view
     */
    public Set<AttributeFilterMapping> getAttributeFilters();
    
    /**
     * Returns the view filter mapping of the entity view with the given name.
     * 
     * @param filterName The name of the view filter mapping which should be returned
     * @return The view filter mapping of the entity view with the given name
     */
    public ViewFilterMapping getViewFilter(String filterName);
    
    /**
     * Returns the view filter mappings of the entity view.
     * 
     * @return The view filter mappings of the entity view
     */
    public Set<ViewFilterMapping> getViewFilters();
}
