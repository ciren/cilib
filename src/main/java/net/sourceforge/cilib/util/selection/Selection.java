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
package net.sourceforge.cilib.util.selection;

import net.sourceforge.cilib.util.selection.arrangement.Arrangement;
import com.google.common.base.Predicate;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.weighting.Weighting;

/**
 *
 * @author gpampara
 */
public final class Selection<T> implements WeighableSelection<T>, PartialSelection<T> {

    private final Object[] elements;

    // need to hide this method somehow
    public static <T> Selection<T> copyOf(Iterable<T> iterable) {
        checkNotNull(iterable);
        return new Selection<T>(copyOfInternal(iterable));
    }

    // Check array size
    private static <T> Object[] copyOfInternal(Iterable<T> iterable) {
        checkArgument(Iterables.size(iterable) >= 1, "Attempting to create a "
                + "selection on an empty collection is not valid.");
        List<T> list = Lists.newArrayList(iterable);
        return list.toArray();
    }

    Selection(Object[] array) {
        this.elements = array;
    }

    @Override
    public Selection<T> exclude(T... items) {
        return exclude(Lists.newArrayList(items));
    }

    @Override
    public Selection<T> exclude(Iterable<T> iterable) {
        List<T> list = Lists.newArrayList(iterable);
        List<T> result = Lists.newArrayList();
        for (Object o : elements) {
            if (!list.contains(o)) {
                result.add((T) o);
            }
        }
        return new Selection<T>(copyOfInternal(result));
    }

    @Override
    public WeightedSelection<T> weigh(Weighting weighing) {
        List<T> list = (List<T>) Lists.newArrayList(elements);
        List<WeightedObject> result = Lists.newArrayList(weighing.weigh(list));
        return new WeightedSelection<T>(result.toArray(new WeightedObject[]{}));
    }

    @Override
    public Selection<T> orderBy(Arrangement arrangement) {
        List<T> list = (List<T>) Lists.newArrayList(elements);
        List<Comparable> result = Lists.newArrayList(arrangement.arrange((Iterable<Comparable>) list));
        return new Selection<T>(result.toArray());
    }

    /**
     * @todo this can be made more efficient: no need to apply the predicate if not needed.
     */
    @Override
    public Selection<T> filter(Predicate<? super T> predicate) {
        List<T> result = Lists.newArrayList();
        for (Object o : elements) {
            if (!predicate.apply((T) o)) {
                result.add((T) o);
            }
        }
        return new Selection<T>(copyOfInternal(result));
    }

    @Override
    public T select() {
        checkState(elements.length >= 1, "Selection is invalid, please verify selection state.");
        return (T) elements[0];
    }

    @Override
    public List<T> select(Samples sample) {
        return (List<T>) sample.sample(Lists.newArrayList(this.elements));
    }
}
