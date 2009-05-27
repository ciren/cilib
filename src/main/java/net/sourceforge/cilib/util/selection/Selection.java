/**
 * Copyright (C) 2003 - 2008
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.util.selection.ordering.Ordering;
import net.sourceforge.cilib.util.selection.weighing.Weighing;

/**
 * <p>
 * A {@code Selection} is an abstraction that allows operations to be applied to
 * a collection instace that result in a selection of list elements, based on a varied of
 * potential combination of operators.
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
 * List<T> selection = Selection.from(population).orderBy(new RandomOrdering<T>()).first(tournamentSize)
 *          .orderBy(new SortedOrdering<T>()).last().select();
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
 * @param <E> The comparable type.
 * @author gpampara
 */
public final class Selection<E> implements SelectionSyntax<E> {

    private List<Entry<E>> elements;

    /**
     * Assign the Selection to take palce on the porvided collection. The
     * collection is copied to ensure that the original collection reference is
     * not altered.
     * @param elements The elements on which the selection should take place.
     */
    private Selection(Collection<? extends E> elements) {
        this.elements = new ArrayList<Entry<E>>(elements.size());

        for (E element : elements) {
            this.elements.add(new Entry<E>(element));
        }
    }

    /**
     * Create a selection that will operate on the provided collection.
     * @param <T> The comparable type.
     * @param elements The collection of elements to operate on.
     * @return A selection based on the provided collection.
     */
    public static <T> Selection<T> from(List<? extends T> elements) {
        return new Selection<T>(elements);
    }

    /**
     * Obtain a random element from the provided list. This is a convenience method
     * that allows the selection of a random element from a provided list, based
     * on a provided {@code Random}.
     * <p>
     * This method terminates the selection.
     * @param <T> The type of the selection.
     * @param elements The element from which the selection is to be performed.
     * @param random The random number to be used in the selection.
     * @return A random element within {@code elements}.
     */
    public static <T> T randomFrom(List<? extends T> elements, Random random) {
        int index = random.nextInt(elements.size());
        int count = 0;

        for (T t : elements) {
            if (count == index)
                return t;

            count++;
        }

        throw new NoSuchElementException("Random element not found?");
    }

    /**
     * Obtain a random element sublist from the provided list. This is a convenience method
     * that allows the selection of random elements from a provided list, based
     * on a provided {@code Random}.
     * <p>
     * This method terminates the selection.
     * @param <T> The selection type.
     * @param elements The elements from which the selection is to be performed.
     * @param random The random number to be used in the selection.
     * @param number The number of elements to select.
     * @return A list of random elements contained in {@code elements}.
     */
    public static <T> List<T> randomFrom(List<? extends T> elements, Random random, int number) {
        List<T> tmp = new ArrayList<T>(number);

        for (int i = 0; i < number; i++) {
            int index = random.nextInt(elements.size());
            tmp.add(elements.get(index));
        }

        return tmp;
    }

    /**
     * Apply the provided ordering on the current selection. The result of the
     * operation will result in a modified selection.
     * @param ordering The ordering to orderBy.
     * @return A selection upon which the ordering has been applied.
     * @throws UnsupportedOperationException if the ordering cannot be applied.
     */
    @Override
    public SelectionSyntax<E> orderBy(Ordering<E> ordering) {
        boolean result = ordering.order(this.elements);

        if (result) {
            return this;
        }

        throw new UnsupportedOperationException("The ordering [" + ordering.getClass().getSimpleName() + "] " +
                "cannot be applied to the selection. Please ensure that the intention of the ordering is correct.");
    }

    /**
     * Apply the provided weighing on the current selection. The result of the
     * operation will result in new weighed selection.
     * @param weighing The weighing to weighWith.
     * @return A selection upon which the weighing has been applied.
     */
    @Override
    public SelectionSyntax<E> weigh(Weighing<E> weighing) {
        boolean result = weighing.weigh(this.elements);

        if (result) {
            return this;
        }

        throw new UnsupportedOperationException("The weighing [" + weighing.getClass().getSimpleName() + "]" +
                "cannot be applied to the selection. Please ensure that the intention of the weighing is correct.");
    }

    /**
     * Obtain the first result from the current selection. These elements are returned
     * from the front of the current selection.
     * @return A selection containing the first element.
     */
    @Override
    public SelectionSyntax<E> first() {
        this.elements = this.elements.subList(0, 1);
        return this;
    }

    /**
     * Obtain the frist {@code number} of elements from the current selection. These
     * elements are returned from the front of the current selection.
     * @param number The number of elements to return.
     * @return A selection containing the first {@code number} elements.
     */
    @Override
    public SelectionSyntax<E> first(int number) {
        this.elements = this.elements.subList(0, number);
        return this;
    }

    /**
     * Obtain the last element contained within the current selection.
     * @return A selection containing the last element.
     */
    @Override
    public SelectionSyntax<E> last() {
        this.elements = this.elements.subList(this.elements.size() - 1, this.elements.size());
        return this;
    }

    /**
     * Obtain the last {@code number} of elements from the current selection.
     * @param number The number of elements to select.
     * @return A selection containing the last {@code number} of elements.
     */
    @Override
    public SelectionSyntax<E> last(int number) {
        this.elements = this.elements.subList(this.elements.size() - number, this.elements.size());
        return this;
    }

    /**
     * Obtain the result of the selection.
     * @return A list of elements that the selection has selected.
     */
    @Override
    public List<E> select() {
        List<E> result = new ArrayList<E>();

        for (Entry<E> entry : elements) {
            result.add(entry.getElement());
        }

        return result;
    }

    /**
     * Obtain the first result of the selection.
     * @return The first element returned by the selection.
     */
    @Override
    public E singleSelect() {
        return this.elements.get(0).getElement();
    }

    /**
     * Obtain the list of internal {@code Entry} instances.
     * @return The list of internal {@code Entry} instances.
     */
    @Override
    public List<Selection.Entry<E>> entries() {
        return this.elements;
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
    public final static class Entry<E> {

        private final E element;
        private double weight;

        /**
         * Create a new {@code Entry}. This constructor is private intentionall
         * @param element The element to decorate.                             +         */
        private Entry(E element) {
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
            return this.element.toString();
        }
    }
}
