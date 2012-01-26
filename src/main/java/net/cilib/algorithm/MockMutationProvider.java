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
import fj.data.List;
import com.google.inject.Inject;
import net.cilib.entity.Entity;
import net.cilib.inject.annotation.Unique;
import static net.cilib.predef.Predef.*;

import java.util.Iterator;


/**
 * @author gpampara
 * @since 0.8
 */
public class MockMutationProvider implements MutationProvider {

    private final Selector selector;
    private double beta = 0.5; // This must be injected

    @Inject
    public MockMutationProvider(@Unique Selector selector) {
        this.selector = selector;
    }

    @Override
    public List<Double> create(Iterable<? extends Entity> iterable) {
        return create(iterable.iterator());
    }

    @Override
    public List<Double> create(final Iterator<? extends Entity> iterator) {
        List<Entity> list = List.iterableList(Lists.newArrayList(iterator));
        List<Double> x1 = selector.select(list).solution();
        List<Double> x2 = selector.select(list).solution();
        List<Double> x3 = selector.select(list).solution();

        List<Double> result = plus(x1, multiply(beta, subtract(x2, x3)));
        return result;
    }
}
