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
public class FixedWeighting implements Weighting {
    private final double weight;

    public FixedWeighting(double weight) {
        this.weight = weight;
    }

    @Override
    public <T> Iterable<WeightedObject> weigh(Iterable<T> iterable) {
        List<WeightedObject> result = Lists.newArrayList();
        for (T t : iterable) {
            result.add(new WeightedObject(t, weight));
        }
        return result;
    }
}
