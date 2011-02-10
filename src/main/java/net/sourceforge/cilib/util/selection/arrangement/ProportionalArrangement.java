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
package net.sourceforge.cilib.util.selection.arrangement;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.util.selection.WeightedObject;

/**
 *
 * @author gpampara
 */
public class ProportionalArrangement<T> implements Arrangement<T> {

    private RandomProvider generator;

    public ProportionalArrangement() {
        this.generator = new MersenneTwister();
    }

    public ProportionalArrangement(RandomProvider generator) {
        this.generator = generator;
    }

    @Override
    public Iterable<T> arrange(Iterable<T> elements) {
        Preconditions.checkArgument(elements.iterator().next() instanceof WeightedObject);
        List<WeightedObject> weightedObjects = (List<WeightedObject>) Lists.newArrayList(elements);
        
        double total = 0.0;
        for (WeightedObject weighedObject : weightedObjects) {
            total += weighedObject.getWeight();
        }

        if (Double.compare(total, 0.0) == 0) {
            return Lists.newArrayList();
        }

        List<WeightedObject> temp = Lists.newArrayList();
        while (weightedObjects.size() > 0) {
            double randomValue = this.generator.nextDouble() * total;
            double marker = 0.0;
            int i = 0;
            do {
                marker += weightedObjects.get(i++).getWeight();
            } while (i < weightedObjects.size() && marker < randomValue);

            WeightedObject selected = weightedObjects.get(i - 1);
            temp.add(selected);
            weightedObjects.remove(i - 1);
            total -= selected.getWeight();
        }

        // The reverse is needed as largest
        // elements were added to the front.
        Collections.reverse(temp);
        weightedObjects.addAll(temp);
        return (Iterable<T>) weightedObjects;
    }
}
