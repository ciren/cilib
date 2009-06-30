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

/**
 * Perform a random selection from the provided list of elements.
 * <p>
 * Random selection is performed by:
 * <ol>
 *   <li>A random element is selected from the provided list.</li>
 *   <li>Return the result.</li>
 * </ol>
 * @author Wiehann Matthysen
 * @param <E>
 */
public class RandomSelection<E> implements SelectionRecipe<E> {
    private static final long serialVersionUID = -5099663528040315048L;

    private Random random;

    /**
     * Create a new instance.
     */
    public RandomSelection() {
        this.random = new MersenneTwister();
    }

    /**
     * Create a new instance with the provided {@code Random}.
     * @param random The {@code random} to use.
     */
    public RandomSelection(Random random) {
        this.random = random;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RandomSelection(RandomSelection copy) {
        this.random = copy.random.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomSelection getClone() {
        return new RandomSelection(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E select(List<? extends E> elements) {
        E selection = Selection.randomFrom(elements, random);
        return selection;
    }
}
