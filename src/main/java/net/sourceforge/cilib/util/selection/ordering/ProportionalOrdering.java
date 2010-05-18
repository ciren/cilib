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
package net.sourceforge.cilib.util.selection.ordering;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.SelectionBuilder;

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

    private RandomProvider generator;

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
    public ProportionalOrdering(RandomProvider generator) {
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

        List<Selection.Entry<E>> temp = Lists.newArrayList();
        while (elements.size() > 0) {
            double randomValue = this.generator.nextDouble() * total;
            double marker = 0.0;
            int i = 0;
            do {
                marker += elements.get(i++).getWeight();
            } while (i < elements.size() && marker < randomValue);

            Selection.Entry<E> selected = elements.get(i - 1);
            temp.add(selected);
            elements.remove(i - 1);
            total -= selected.getWeight();
        }

        // The reverse is needed as largest
        // elements were added to the front.
        Collections.reverse(temp);

        elements.addAll(temp);

        return true;
    }
}
