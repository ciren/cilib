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
package net.sourceforge.cilib.util.selection.recipes;

import java.util.List;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.ordering.ProportionalOrdering;
import net.sourceforge.cilib.util.selection.weighing.LinearWeighing;
import net.sourceforge.cilib.util.selection.weighing.Weighing;

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
 * @author Wiehann Matthysen
 */
public class RouletteWheelSelection<E extends Comparable> implements SelectionRecipe<E> {
    private static final long serialVersionUID = 4194450350205390514L;

    private Weighing<E> weighing;
    private Random random;

    /**
     * Create a new instance.
     */
    public RouletteWheelSelection() {
        this.weighing = new LinearWeighing<E>();
        this.random = new MersenneTwister();
    }

    /**
     * Create a new instance with the provided weighing strategy.
     * @param weighing The weighing strategy to set.
     */
    public RouletteWheelSelection(Weighing<E> weighing) {
        this.weighing = weighing;
        this.random = new MersenneTwister();
    }

    public RouletteWheelSelection(Weighing<E> weighing, Random random) {
        this.weighing = weighing;
        this.random = random;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RouletteWheelSelection(RouletteWheelSelection copy) {
        this.weighing = copy.weighing.getClone();
        this.random = copy.random.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RouletteWheelSelection getClone() {
        return new RouletteWheelSelection(this);
    }

    /**
     * Set the weighing strategy
     * @param weighing The strategy to set.
     */
    public void setWeighing(Weighing<E> weighing) {
        this.weighing = weighing;
    }

    /**
     * Get the current weighing strategy.
     * @return The current weighing strategy.
     */
    public Weighing<E> getWeighing() {
        return this.weighing;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E select(List<? extends E> elements) {
        E selection = Selection.from(elements).weigh(this.weighing).orderBy(new ProportionalOrdering<E>(this.random)).last().singleSelect();
        return selection;
    }
}
