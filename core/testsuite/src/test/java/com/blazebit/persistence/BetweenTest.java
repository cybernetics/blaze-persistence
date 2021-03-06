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

import com.blazebit.persistence.entity.Document;
import static com.googlecode.catchexception.CatchException.verifyException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Christian Beikov
 * @author Moritz Becker
 * @since 1.0
 */
public class BetweenTest extends AbstractCoreTest {

    @Test
    public void testBetween() {
        CriteriaBuilder<Document> criteria = cbf.from(em, Document.class, "d");
        criteria.where("d.age").between(1L, 10L);

        assertEquals("SELECT d FROM Document d WHERE d.age BETWEEN :param_0 AND :param_1", criteria.getQueryString());
        criteria.getResultList();
    }

    @Test
    public void testBetweenValueAndNull() {
        CriteriaBuilder<Document> criteria = cbf.from(em, Document.class, "d");
        verifyException(criteria.where("d.age"), NullPointerException.class).between(1, null);
    }

    @Test
    public void testBetweenNullAndValue() {
        CriteriaBuilder<Document> criteria = cbf.from(em, Document.class, "d");
        verifyException(criteria.where("d.age"), NullPointerException.class).between(null, 10);
    }

    @Test
    public void testNotBetween() {
        CriteriaBuilder<Document> criteria = cbf.from(em, Document.class, "d");
        criteria.where("d.age").notBetween(1L, 10L);

        assertEquals("SELECT d FROM Document d WHERE NOT d.age BETWEEN :param_0 AND :param_1", criteria.getQueryString());
        criteria.getResultList();
    }

    @Test
    public void testNotBetweenValueAndNull() {
        CriteriaBuilder<Document> criteria = cbf.from(em, Document.class, "d");
        verifyException(criteria.where("d.age"), NullPointerException.class).notBetween(1, null);
    }

    @Test
    public void testNotBetweenNullAndValue() {
        CriteriaBuilder<Document> criteria = cbf.from(em, Document.class, "d");
        verifyException(criteria.where("d.age"), NullPointerException.class).notBetween(null, 10);
    }
}
