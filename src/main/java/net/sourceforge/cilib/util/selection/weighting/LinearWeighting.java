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
package net.sourceforge.cilib.util.selection.weighting;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.util.selection.WeightedObject;

/**
 *
 * @author gpampara
 */
public class LinearWeighting implements Weighting {

    private double min;
    private double max;

    public LinearWeighting() {
        this(0.0, 1.0);
    }

    public LinearWeighting(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public <T> Iterable<WeightedObject> weigh(Iterable<T> iterable) {
        List<T> elements = Lists.newArrayList(iterable);
        List<WeightedObject> results = Lists.newArrayListWithExpectedSize(elements.size());

        double stepSize = (this.max - this.min) / (elements.size() - 1);
        int objectIndex = 0;
        for (T element : elements) {
            results.add(new WeightedObject(element, objectIndex++ * stepSize + this.min));
        }
        return results;
    }
}
