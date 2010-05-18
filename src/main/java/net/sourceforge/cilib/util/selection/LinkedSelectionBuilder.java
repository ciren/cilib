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

import com.google.common.base.Predicate;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.util.selection.ordering.Ordering;
import net.sourceforge.cilib.util.selection.weighing.Weighing;

/**
 *
 */
public interface LinkedSelectionBuilder<T> {

     /**
     * Apply the provided ordering on the current selection. The result of the
     * operation will result in a modified selection.
     * @param ordering The ordering to orderBy.
     * @return A selection upon which the ordering has been applied.
     * @throws UnsupportedOperationException if the ordering cannot be applied.
     */
    SelectionBuilder<T> orderBy(Ordering<T> ordering);

    /**
     * Apply the provided weighing on the current selection. The result of the
     * operation will result in new weighed selection.
     * @param weighing The weighing to weighWith.
     * @return A selection upon which the weighing has been applied.
     */
    SelectionBuilder<T> weigh(Weighing<T> weighing);

    /**
     * Obtain a random element from the current Selection.
     * @param random The random number to be used in the selection.
     * @return A selection containing a random element from the original {@code elements} member.
     */
    SelectionBuilder<T> random(RandomProvider random);

    /**
     * Obtain a random number of elements from the current Selection.
     * @param random The random number to be used in the selection.
     * @param number The number of elements to select.
     * @return A selection containing the random elements from the original {@code elements} member.
     */
    SelectionBuilder<T> random(RandomProvider random, int number);

    /**
     * Remove any {@code Entry}'s from {@code elements} that are also contained in {@code exclusion}.
     * @param exclusions The elements to exclude.
     * @return A selection containing the remaining elements which do not occur in {@code exclusion}.
     */
    SelectionBuilder<T> exclude(T... exclusions);

    /**
     * Remove any {@code Entry}'s from {@code elements} that are also contained in {@code exclusion}.
     * @param exclusions The elements to exclude.
     * @return A selection containing the remaining elements which do not occur in {@code exclusion}.
     */
    SelectionBuilder<T> exclude(Iterable<T> exclusions);

    /**
     * Remove any {@code Entry}'s from {@code elements} that does not satisfy {@code predicate}.
     * @param predicate The predicate that tests if an element would should be removed or not.
     * @return A selection containing the remaining elements which satisfies the {@code predicate}.
     */
    SelectionBuilder<T> filter(Predicate<? super T> predicate);

    /**
     * Obtain the list of internal {@code Entry} instances.
     * @return The list of internal {@code Entry} instances.
     */
    List<Selection.Entry<T>> entries();

}
