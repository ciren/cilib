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
package net.sourceforge.cilib.util.selection.ordering;

import java.util.List;
import java.util.Random;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.util.selection.Selection;

/**
 * Apply a proportionate ordering. Proportionate ordering is done by determining
 * the total maximum weight and then determining the percentage share for each
 * entry.
 * <p>
 * <a href=http://en.wikipedia.org/wiki/Fitness_proportionate_selection>Some more information</a>
 * @param <E> The selection type.
 * @author Wiehann Matthysen
 */
public class ProportionalOrdering<E> implements Ordering<E> {

    private Random generator;

    /**
     * Create a new instance with an internal {@link MersenneTwister}.
     */
    public ProportionalOrdering() {
        this.generator = new MersenneTwister();
    }

    /**
     * Create a new instance with the provided {@link Random}.
     * @param generator The generator to use.
     */
    public ProportionalOrdering(Random generator) {
        this.generator = generator;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method will apply the proportionate ordering on the provided
     * list of elements.
     */
    @Override
    public boolean order(List<Selection.Entry<E>> elements) {
        double total = 0.0;
        for (Selection.Entry<E> weighedObject : elements) {
            total += weighedObject.getWeight();
        }

        if (Double.compare(total, 0.0) == 0) {
            return false;
        }

        for (int i = 0; i < elements.size(); ++i) {
            double randomValue = this.generator.nextDouble() * total;
            double marker = 0.0;
            int j = i;
            do {
                marker += elements.get(j++).getWeight();
            } while (j < elements.size() && marker >= randomValue);
            // Swap elements i and j - 1.
            Selection.Entry<E> elementJ = elements.set(j - 1, elements.set(i, elements.get(j - 1)));
            total -= elementJ.getWeight();
        }
        return true;
    }
}
