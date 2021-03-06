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
package com.blazebit.persistence.spi;

import com.blazebit.persistence.CriteriaBuilderFactory;
import java.util.List;
import java.util.Properties;

/**
 * A configuration for a {@link CriteriaBuilderFactory} which is mostly used in non Java EE environments.
 *
 * @author Christian Beikov
 * @since 1.0
 */
public interface CriteriaBuilderConfiguration {

    /**
     * Registers the given query transformer in the configuration.
     *
     * @param queryTransformer The transformer that should be addded
     * @return this for method chaining
     */
    public CriteriaBuilderConfiguration registerQueryTransformer(QueryTransformer queryTransformer);

    /**
     * Returns a list of registered query transformers.
     *
     * @return A list of registered query transformers
     */
    public List<QueryTransformer> getQueryTransformers();

    /**
     * Creates a new {@linkplain CriteriaBuilderFactory} based on this configuration.
     *
     * @return A new {@linkplain CriteriaBuilderFactory}
     */
    public CriteriaBuilderFactory createCriteriaBuilderFactory();

    /**
     * Returns all properties.
     *
     * @return All properties
     */
    public Properties getProperties();

    /**
     * Returns a property value by name.
     *
     * @param propertyName The name of the property
     * @return The value currently associated with that property name; may be null.
     */
    public String getProperty(String propertyName);

    /**
     * Replace the properties of the configuration with the given properties.
     *
     * @param properties The new set of properties
     * @return this for method chaining
     */
    public CriteriaBuilderConfiguration setProperties(Properties properties);

    /**
     * Add the given properties to the properties of the configuration.
     *
     * @param extraProperties The properties to add.
     * @return this for method chaining
     *
     */
    public CriteriaBuilderConfiguration addProperties(Properties extraProperties);

    /**
     * Adds the given properties to the properties of the configuration, without overriding existing values.
     *
     * @param properties The properties to merge
     * @return this for method chaining
     */
    public CriteriaBuilderConfiguration mergeProperties(Properties properties);

    /**
     * Set a property value by name.
     *
     * @param propertyName The name of the property to set
     * @param value        The new property value
     * @return this for method chaining
     */
    public CriteriaBuilderConfiguration setProperty(String propertyName, String value);
}
