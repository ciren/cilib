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

import com.google.common.base.Predicate;
import java.util.List;
import net.sourceforge.cilib.util.selection.ordering.Ordering;
import net.sourceforge.cilib.util.selection.weighing.Weighing;

/**
 * Default operations related to selection.
 * @author Wiehann Matthysen
 * @param <E> The selection type.
 */
public interface SelectionSyntax<E> {

    /**
     * Apply the provided ordering on the current selection. The result of the
     * operation will result in a modified selection.
     * @param ordering The ordering to orderBy.
     * @return A selection upon which the ordering has been applied.
     * @throws UnsupportedOperationException if the ordering cannot be applied.
     */
    public SelectionSyntax<E> orderBy(Ordering<E> ordering);

    /**
     * Apply the provided weighing on the current selection. The result of the
     * operation will result in new weighed selection.
     * @param weighing The weighing to weighWith.
     * @return A selection upon which the weighing has been applied.
     */
    public SelectionSyntax<E> weigh(Weighing<E> weighing);

    /**
     * Obtain the first result from the current selection. These elements are returned
     * from the front of the current selection.
     * @return A selection containing the first element.
     */
    public SelectionSyntax<E> first();

    /**
     * Obtain the first {@code number} of elements from the current selection. These
     * elements are returned from the front of the current selection.
     * @param number The number of elements to return.
     * @return A selection containing the first {@code number} elements.
     */
    public SelectionSyntax<E> first(int number);

    /**
     * Obtain the last element contained within the current selection.
     * @return A selection containing the last element.
     */
    public SelectionSyntax<E> last();

    /**
     * Obtain the last {@code number} of elements from the current selection.
     * @param number The number of elements to select.
     * @return A selection containing the last {@code number} of elements.
     */
    public SelectionSyntax<E> last(int number);

    /**
     * Remove any {@code Entry}'s from {@code elements} that are also contained in {@code exclusion}.
     * @param exclusions The elements to exclude.
     * @return A selection containing the remaining elements which do not occur in {@code exclusion}.
     */
    public SelectionSyntax<E> exclude(E... exclusions);

    /**
     * Remove any {@code Entry}'s from {@code elements} that are also contained in {@code exclusion}.
     * @param exclusions The elements to exclude.
     * @return A selection containing the remaining elements which do not occur in {@code exclusion}.
     */
    public SelectionSyntax<E> exclude(Iterable<E> exclusions);

    /**
     * Remove any {@code Entry}'s from {@code elements} that does not satisfy {@code predicate}.
     * @param predicate The predicate that tests if an element will be removed or not.
     * @return A selection containing the remaining elements which satisfies {@code predicate}.
     */
    public SelectionSyntax<E> satisfies(Predicate<? super E> predicate);

    /**
     * Obtain the result of the selection.
     * @return A list of elements that the selection has selected.
     */
    public List<E> select();

    /**
     * Obtain the list of internal {@code Entry} instances.
     * @return The list of internal {@code Entry} instances.
     */
    public List<SelectionSyntax.Entry<E>> entries();

    /**
     * Obtain the first result of the selection.
     * @return The first element returned by the selection.
     */
    public E singleSelect();


    /**
     * This class provides the notion of an entry within a list
     * for the selection process.
     * <p>
     * This class is is final and non-instantiable to ensure that the
     * operations are allowed to be applied, however, additional metadata
     * can be recored and used during the selection process.
     * @param <E> The {@see Comparable} type.
     */
    interface Entry<E> {

        /**
         * Get the {@code element} that this {@code Entry} represents.
         * @return The decorated {@code element}.
         */
        public E getElement();

        /**
         * Obtain the weight value associated with this {@code Entry}.
         * <p>
         * The weight value need not be set. It is not always used.
         *
         * @return The {@code weight} value.
         */
        public double getWeight();

        /**
         * Set the weight associated to the current {@code Entry}.
         * @param value The weight value to set.
         */
        public void setWeight(double value);
    }
}
