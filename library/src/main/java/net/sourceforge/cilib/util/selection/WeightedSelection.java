/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.util.selection.arrangement.Arrangement;

/**
 *
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
        return Arrays.asList(elements);
    }
}
