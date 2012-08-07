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

import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.ProportionalArrangement;
import net.sourceforge.cilib.util.selection.arrangement.ReverseArrangement;
import net.sourceforge.cilib.util.selection.arrangement.SortedArrangement;
import net.sourceforge.cilib.util.selection.weighting.LinearWeighting;
import net.sourceforge.cilib.util.selection.weighting.Weighting;

/**
 * A recipe for Roulette wheel selection.
 * <p>
 * Roulette wheel selection is performed by:
 * <ol>
 *   <li>Weighing the elements of a selection.</li>
 *   <li>Performing a proportional ordering of the weighed elements.</li>
 *   <li>Returning the best result.</li>
 * </ol>
 * @param <E> The selection type.
 */
public class RouletteWheelSelector<E extends Comparable> implements Selector<E> {

    private static final long serialVersionUID = 4194450350205390514L;
    private Weighting weighting;
    private RandomProvider random;

    /**
     * Create a new instance.
     */
    public RouletteWheelSelector() {
        this.weighting = new LinearWeighting();
        this.random = new MersenneTwister();
    }

    /**
     * Create a new instance with the provided weighing strategy.
     * @param weighing The weighing strategy to set.
     */
    public RouletteWheelSelector(Weighting weighing) {
        this.weighting = weighing;
        this.random = new MersenneTwister();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RouletteWheelSelector(RouletteWheelSelector<E> copy) {
        this.weighting = copy.weighting;
        this.random = copy.random;
    }

    /**
     * Set the weighing strategy
     * @param weighing The strategy to set.
     */
    public void setWeighing(Weighting weighing) {
        this.weighting = weighing;
    }

    /**
     * Get the current weighing strategy.
     * @return The current weighing strategy.
     */
    public Weighting getWeighing() {
        return this.weighting;
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
        return Selection.copyOf(iterable).weigh(weighting)
                .orderBy(new SortedArrangement())
                .orderBy(new ProportionalArrangement())
                .orderBy(new ReverseArrangement());
    }
}
