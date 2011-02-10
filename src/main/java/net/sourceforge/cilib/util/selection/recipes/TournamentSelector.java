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
package net.sourceforge.cilib.util.selection.recipes;

import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.RandomArrangement;
import net.sourceforge.cilib.util.selection.arrangement.ReverseArrangement;
import net.sourceforge.cilib.util.selection.arrangement.SortedArrangement;

/**
 * A recipe for Tournament selection.
 * <p>
 * Tournament selection is performed by:
 * <ol>
 *   <li>Randomly ordering a list of elements.</li>
 *   <li>Selecting a sublist of {@code tournamentSize}.</li>
 *   <li>Sorting the created sublist.</li>
 *   <li>Selecting the best performing element.</li>
 *   <li>Return the result.</li>
 * </ol>
 *
 * @param <E> The selection type.
 * @author Wiehann Matthysen
 */
public class TournamentSelector<E extends Comparable<? super E>> implements Selector<E> {

    private static final long serialVersionUID = -6689673224380247931L;
    private ControlParameter tournamentProportion;
    private Comparator<E> comparator;
    private RandomProvider random;

    /**
     * Create a new instance.
     */
    public TournamentSelector() {
        this.tournamentProportion = new ProportionalControlParameter();
        this.comparator = Ordering.natural();
        this.random = new MersenneTwister();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public TournamentSelector(TournamentSelector<E> copy) {
        this.tournamentProportion = copy.tournamentProportion.getClone();
        this.comparator = copy.comparator;
        this.random = copy.random;
    }

    /**
     * Get the size of the tournament.
     * @return The size of the tournament.
     */
    public ControlParameter getTournamentSize() {
        return this.tournamentProportion;
    }

    /**
     * Set the size of the tournament.
     * @param tournamanetSize The value to set.
     */
    public void setTournamentSize(ControlParameter tournamanetSize) {
        this.tournamentProportion = tournamanetSize;
    }

    /**
     * Set the comparator for the selection.
     * @param comparator The value to set.
     */
    public void setComparator(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Get the comparator for the selection.
     * @return The current comparator.
     */
    public Comparator<E> getComparator() {
        return this.comparator;
    }

    /**
     * Set the random number generator to use.
     * @param random The value to set.
     */
    public void setRandom(RandomProvider random) {
        this.random = random;
    }

    /**
     * Get the current random number generator.
     * @return The current random number generator.
     */
    public RandomProvider getRandom() {
        return this.random;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PartialSelection<E> on(Iterable<E> iterable) {
        int size = Iterables.size(iterable);
        int tournamentSize = Double.valueOf(this.tournamentProportion.getParameter() * size).intValue();
        List<E> intermediate = Selection.copyOf(iterable).orderBy(new RandomArrangement(random)).select(Samples.last(tournamentSize));
        return Selection.copyOf(intermediate).orderBy(new SortedArrangement()).orderBy(new ReverseArrangement());
    }
}
