/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.cilib.algorithm;

import fj.data.Option;
import net.cilib.collection.immutable.CandidateSolution;
import net.cilib.entity.Entity;
import net.cilib.entity.FitnessComparator;
import net.cilib.entity.Individual;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;

public class ReplacementSelectorTest {

    @Test
    public void selection() {
        Selector selector = new ReplacementSelector(FitnessComparator.MAX);

        Entity e = new Individual(CandidateSolution.empty(), Option.some(0.0));
        Individual i = new Individual(CandidateSolution.empty(), Option.some(1.0));

        Entity r = selector.select(e, i);

        Assert.assertThat(r, ReferenceMatcher.sameReference(i));
    }

//    @Test
//    public void getBestInstanceFromListOfInstances() {
//        Selector selector = new ReplacementSelector(FitnessComparator.MAX);
//
//               
//    }

    static class ReferenceMatcher extends BaseMatcher<Object> {

        private final Object object;

        public ReferenceMatcher(Object obj) {
            object = obj;
        }

        @Override
        public boolean matches(Object item) {
            return object == item;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Determine if the provided object is the same reference as the target.");
        }

        public static Matcher<Object> sameReference(Object obj) {
            return new ReferenceMatcher(obj);
        }
    }
}
