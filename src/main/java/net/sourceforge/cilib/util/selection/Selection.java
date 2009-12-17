/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.util.selection;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import net.sourceforge.cilib.util.selection.ordering.Ordering;
import net.sourceforge.cilib.util.selection.weighing.Weighing;

/**
 * <p>
 * A {@code Selection} is an abstraction that allows operations to be applied to
 * a collection instance that result in a selection of list elements, based on
 * a variety of potential combination of operators.
 * </p>
 * <p>
 * The {@code Selection} is implemented to be a fluent interface that is easily
 * understandable and for which the intent of the selection is clear.
 * </p>
 * <p>
 * As an example, applying tournament selection on a list of {@code Entity} instances
 * would be done as follows:
 * </p>
 * <pre>
 * List&lt;T&gt; selection = Selection.from(population).orderBy(new RandomOrdering&lt;T&gt;())
 *      .select(Samples.first(tournamentSize)).and().orderBy(new SortedOrdering&lt;T&gt;())
 *      .select(Samples.last()).perform();
 *
 * return selection.get(0);
 * </pre>
 * <p>
 * where {@code T} is a {@link Comparable} type. The above performs the following steps:
 * </p>
 * <ol>
 *   <li>From the provided list of entities, order the entities randomly.</li>
 *   <li>From the randomized list, select the first {@code tournamentSize} entities.</li>
 *   <li>From the subset of elements, order them from smallest to largest, based on fitness.</li>
 *   <li>Finally, select the entity that is the "most fit" and then return it as the winner of the tournament.</li>
 * </ol>
 * @param <T> The comparable type.
 * @author gpampara
 */
public final class Selection<T> implements LinkedSelectionBuilder<T>,
        LinkedUniqueSelectionBuilder<T>, SelectionBuilder<T>, SampleSelectionBuilder<T> {
    private final List<Entry<T>> elements;
    private final RandomSelection<Selection.Entry<T>> randomSelection;

    /**
     * Assign the Selection to take palce on the porvided collection. The
     * collection is copied to ensure that the original collection reference is
     * not altered.
     * @param elements The elements on which the selection should take place.
     */
    private Selection(List<Selection.Entry<T>> elements, RandomSelection<Selection.Entry<T>> randomSelection) {
        this.elements = elements;
        this.randomSelection = randomSelection;
    }

    /**
     * Create a selection that will operate on the provided collection.
     * @param <T> The comparable type.
     * @param elements The collection of elements to operate on.
     * @return A selection based on the provided collection.
     * @throws IllegalArgumentException if the provided list is empty.
     */
    public static <T> LinkedUniqueSelectionBuilder<T> from(List<? extends T> elements) {
        Preconditions.checkArgument(elements.size() > 0, "Cannot perform a selection on a zero length collection.");
        List<Selection.Entry<T>> list = Lists.newArrayListWithCapacity(elements.size());

        for (T element : elements) {
            list.add(new Selection.Entry<T>(element));
        }

        return new Selection<T>(list, new DefaultRandomSelection<Selection.Entry<T>>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedSelectionBuilder<T> unique(){
        return new Selection<T>(elements, new UniqueRandomSelection<Selection.Entry<T>>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionBuilder<T> orderBy(Ordering<T> ordering) {
        boolean result = ordering.order(this.elements);

        if (result) {
            return this;
        }

        throw new UnsupportedOperationException("The ordering [" + ordering.getClass().getSimpleName() + "] " +
                "cannot be applied to the selection. Please ensure that the intention of the ordering is correct.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionBuilder<T> weigh(Weighing<T> weighing) {
        boolean result = weighing.weigh(this.elements);

        if (result) {
            return this;
        }

        throw new UnsupportedOperationException("The weighing [" + weighing.getClass().getSimpleName() + "]" +
                "cannot be applied to the selection. Please ensure that the intention of the weighing is correct.");
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * <b>Note:</b> This method forcefully breaks out of the Selection-EDSL.
     * </p>
     */
    @Override
    public List<Selection.Entry<T>> entries() {
        return this.elements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Selection<T> exclude(T... exclusions) {
        return exclude(Lists.newArrayList(exclusions));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Selection<T> exclude(Iterable<T> exclusions) {
        List<Selection.Entry<T>> tmp = Lists.newArrayList();

        for (T e : exclusions) {
            for (Selection.Entry<T> entry : this.elements)
                if (entry.getElement().equals(e))
                    tmp.add(entry);
        }

        this.elements.removeAll(tmp);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Selection<T> filter(final Predicate<? super T> predicate) {
        Predicate<Selection.Entry<T>> internal = new Predicate<Selection.Entry<T>>() {
            @Override
            public boolean apply(Selection.Entry<T> input) {
                return !predicate.apply(input.getElement());
            }
        };

        Iterable<Selection.Entry<T>> iterable = Iterables.filter(elements, internal);
        List<Selection.Entry<T>> tmp = Lists.newArrayList();
        Iterables.addAll(tmp, iterable);
        this.elements.removeAll(tmp);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Selection<T> random(Random random) {
        Selection.Entry<T> randomEntry = this.randomSelection.get(this.elements, random);
        this.elements.clear();
        this.elements.add(randomEntry);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Selection<T> random(Random random, int number) {
        List<Selection.Entry<T>> tmp = randomSelection.get(this.elements, random, number);
        elements.clear();
        elements.addAll(tmp);
        return this;
    }

    @Override
    public LinkedSelectionBuilder<T> and() {
        return this;
    }

    @Override
    public SampleSelectionBuilder<T> select(SamplePredicate<? super T> action) {
        List<Selection.Entry<T>> list = Lists.newArrayListWithCapacity(elements.size());

        for (Selection.Entry<T> element : elements) {
            if (!action.isDone()) {
                if (action.apply(element.getElement(), elements.size())) {
                    list.add(element);
                }
            }
        }

        // Now we need to clear the current structure and add the
        // selected elements, based on the provided SamplePreidcate
        this.elements.clear();
        this.elements.addAll(list);

        return this;
    }

    /**
     * 
     * @return
     */
    @Override
    public List<T> perform() {
        return Lists.transform(elements, new Function<Selection.Entry<T>, T>(){
            @Override
            public T apply(Entry<T> from) {
                return from.element;
            }
        });
    }

    /**
     * Obtain the first element. This is a helper method that has been
     * provided to try make the API usage neater. This method simply
     * performs
     * <pre>
     *     List.get(0)
     * </pre>
     * instead of returning the list for the user to manipulate.
     * <p>
     * <b>Note:</b> This method will throw away the underlying collection
     * structure.
     * @return The first element within the selection result.
     */
    @Override
    public T performSingle() {
        Preconditions.checkState(elements.size() >= 1, "Selection must result in a return result.");
        return elements.get(0).getElement();
    }

    /**
     * This class provides the notion of an entry within a list
     * for the selection process.
     * <p>
     * This class is is final and non-instantiable to ensure that the
     * operations are allowed to be applied, however, additional metadata
     * can be recored and used during the selection process.
     * @param <E> The {@see Comparable} type.
     */
    public static class Entry<E> {
        private final E element;
        private double weight;

        /**
         * Create a new {@code Entry}. This constructor is private intentionall
         * @param element The element to decorate.
         */
        Entry(E element) {
            this.element = element;
            this.weight = 0.0;
        }

        /**
         * Get the {@code element} that this {@code Entry} represents.
         * @return The decorated {@code element}.
         */
        public E getElement() {
            return element;
        }

        /**
         * Obtain the weight value associated with this {@code Entry}.
         * <p>
         * The weight value need not be set. It is not always used.
         *
         * @return The {@code weight} value.
         */
        public double getWeight() {
            return this.weight;
        }

        /**
         * Set the {@code weight} value for the current {@code Entry}
         * within the {@code Selection}.
         * @param weight The {@code weight} value to set.
         */
        public void setWeight(double weight) {
            this.weight = weight;
        }

        /**
         * Determine if the provided {@code obj} is equal to the currently
         * decorated element within this {@code Entry}.
         * @param obj The object instance to compare.
         * @return {@code true} if the objects are equal, {@code false} otherwi
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if ((obj == null) || (this.getClass() != obj.getClass())) {
                return false;
            }

            // This suppression is type safe, based on the above checks
            @SuppressWarnings("unchecked")
            Entry<E> other = (Entry<E>) obj;
            return this.element.equals(other.element);
        }

        /**
         * Obtain the hash of the decorated {@code element}.
         * @return The decorated instance's hash value.
         */
        @Override
        public int hashCode() {
            return element.hashCode();
        }

        /**
         * Obtain the {@code String} of the decorated {@code element}.
         * @return The {@code toString()} of the decorated element.
         */
        @Override
        public String toString() {
            return this.element.toString() + ":" + this.weight;
        }
    }

    interface RandomSelection<E> {
        E get(List<E> elements, Random random);
        List<E> get(List<E> elements, Random random, int number);
    }

    static final class DefaultRandomSelection<E> implements RandomSelection<E> {
        @Override
        public E get(List<E> elements, Random random) {
            Preconditions.checkArgument(elements.size() > 0, "Provided list must contain elements.");
            int index = random.nextInt(elements.size());
            int count = 0;

            for (E t : elements) {
                if (count == index)
                    return t;

                count++;
            }

            throw new NoSuchElementException("Random element not found?");
        }

        @Override
        public List<E> get(List<E> elements, Random random, int number) {
            Preconditions.checkArgument(elements.size() > 0, "Provided list must contain elements.");
            List<E> tmp = Lists.newArrayListWithCapacity(number);

            for (int i = 0; i < number; i++) {
                int index = random.nextInt(elements.size());
                tmp.add(elements.get(index));
            }

            return tmp;
        }
    }

    static final class UniqueRandomSelection<E> implements RandomSelection<E> {
        @Override
        public E get(List<E> elements, Random random) {
            Preconditions.checkArgument(elements.size() > 0, "Provided list must contain elements.");
            int index = random.nextInt(elements.size());
            int count = 0;

            for (E t : elements) {
                if (count == index) {
                    return t;
                }

                count++;
            }
            throw new NoSuchElementException("Random element not found?");
        }

        /**
         * This implementation determines unique elements from a collection. The method
         * operates by having all duplicate items removed by placing all elements into
         * a {@linkplain java.util.Set set}. Then from the set, a selection is made.
         * @param elements The elements to select from.
         * @param random The provided random number generator.
         * @param number The number of items to select.
         * @return A List of unique items.
         */
        @Override
        public List<E> get(final List<E> elements, Random random, int number) {
            Preconditions.checkArgument(number <= elements.size(),
                    "Unable to select " + number + " unique elements, current selection only contains " + elements.size() + " elements.");

            // Remove the possible duplicates in 'elements'
            List<E> from = Lists.newArrayList(Sets.newHashSet(elements));
            Preconditions.checkState(from.size() >= number, "Not enough unique elements in the provided list.");
            List<E> to = Lists.newArrayList();

            for (int i = 0; i < number; i++) {
                int index = random.nextInt(from.size());
                to.add(from.get(index));
                from.remove(index); // Remove the selected entry fromt he original list.
            }

            return to;
        }
    }
}
