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

import java.util.List;
import net.sourceforge.cilib.util.selection.arrangement.Arrangement;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 *
 * @author gpampara
 */
public class WeightedSelection<T> implements PartialSelection<T> {

    private final WeightedObject[] elements;

    public static <T> WeightedSelection<T> copyOf(Iterable<T> iterable) {
        return new WeightedSelection<T>(copyOfInternal(iterable));
    }

    // Check array size
    private static <T> WeightedObject[] copyOfInternal(Iterable<T> iterable) {
        checkArgument(Iterables.size(iterable) >= 1, "Attempting to create a "
                + "selection on an empty collection is not valid.");
        List<T> list = Lists.newArrayList(iterable);
        List<WeightedObject> result = Lists.newArrayListWithExpectedSize(list.size());
        for (T t : list) {
            result.add(new WeightedObject(t, 0.0));
        }
        return result.toArray(new WeightedObject[]{});
    }

    WeightedSelection(WeightedObject[] array) {
        this.elements = array;
    }

    @Override
    public PartialSelection<T> exclude(T... items) {
        return exclude(Lists.newArrayList(items));
    }

    @Override
    public WeightedSelection<T> orderBy(Arrangement arrangement) {
        List<WeightedObject> list = Lists.newArrayList(elements);
        List<WeightedObject> result = Lists.newArrayList(arrangement.arrange(list));
        return new WeightedSelection<T>(result.toArray(new WeightedObject[]{}));
    }

    @Override
    public PartialSelection<T> exclude(Iterable<T> items) {
        List<T> list = Lists.newArrayList(items);
        List<T> result = Lists.newArrayList();
        for (WeightedObject o : elements) {
            if (!list.contains(o.getObject())) {
                result.add((T) o.getObject());
            }
        }
        return new WeightedSelection<T>(copyOfInternal(result));
    }

    @Override
    public PartialSelection<T> filter(Predicate<? super T> predicate) {
        List<T> result = Lists.newArrayList();
        for (WeightedObject o : elements) {
            if (!predicate.apply((T) o.getObject())) {
                result.add((T) o.getObject());
            }
        }
        return new WeightedSelection<T>(copyOfInternal(result));
    }

    @Override
    public T select() {
        checkState(elements.length >= 1, "Selection is invalid, please verify selection state.");
        return (T) this.elements[0].getObject();
    }

    @Override
    public List<T> select(Samples sampler) {
        List<T> list = Lists.newArrayList();
        for (WeightedObject element : elements) {
            list.add((T) element.getObject());
        }
        return (List<T>) sampler.sample(list);
    }

    public List<WeightedObject> weightedElements() {
        return ImmutableList.of(elements);
    }
}
