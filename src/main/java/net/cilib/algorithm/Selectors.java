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

import com.google.common.collect.Lists;
import net.cilib.entity.Entity;

/**
 * /?????????????????????????????????????????????????????
 * NOT SUURE ABOUT THIS API
 * 
 * @author gpampara
 */
public final class Selectors {

    private Selectors() {}

    public static <A> Selector newExcludingSelector(Selector selector, A... excludes) {
        return new ExcludingSelector<A>(excludes, selector);
    }

    private static class ExcludingSelector<A> implements Selector {
        private final A[] entities;
        private final Selector selector;

        public ExcludingSelector(A[] excludes, Selector selector) {
            this.entities = excludes;
            this.selector = selector;
        }

        @Override
        public Entity select(Entity... elements) {
            return select(Lists.newArrayList(elements));
        }

        @Override
        public Entity select(Iterable<Entity> elements) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
