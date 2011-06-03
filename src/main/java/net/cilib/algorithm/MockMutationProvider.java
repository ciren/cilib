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
import com.google.inject.Inject;
import net.cilib.collection.MutableSeq;
import net.cilib.collection.Seq;
import net.cilib.collection.immutable.CandidateSolution;
import net.cilib.entity.Entity;
import net.cilib.inject.annotation.Unique;

import java.util.Iterator;
import java.util.List;

import static net.cilib.collection.MutableSeq.multiply;

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
    public CandidateSolution create(Iterable<? extends Entity> iterable) {
        return create(iterable.iterator());
    }

    @Override
    public CandidateSolution create(Iterator<? extends Entity> iterator) {
        List<Entity> list = Lists.newArrayList(iterator);
        MutableSeq x1 = selector.select(list).solution().toMutableSeq();
        MutableSeq x2 = selector.select(list).solution().toMutableSeq();
        MutableSeq x3 = selector.select(list).solution().toMutableSeq();

        Seq result = multiply(beta, x1.plus(x2.subtract(x3)));
        return CandidateSolution.copyOf(result);
    }
}
