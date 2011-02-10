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

import com.google.common.collect.Lists;
import java.util.List;

/**
 *
 * @author gpampara
 */
public class RingBasedArrangement<T> implements Arrangement<T> {

    private T marker;

    public RingBasedArrangement(T marker) {
        this.marker = marker;
    }

    @Override
    public Iterable<T> arrange(Iterable<T> elements) {
        List<T> tmp = Lists.newArrayList(elements);
        List<T> result = Lists.newArrayListWithCapacity(tmp.size());

        int position = 0;
        for (T entry : elements) {
            if (this.marker.equals(entry)) {
                break;
            }
            position++;
        }

        for (int i = 0; i < tmp.size(); ++i) {
            result.add(tmp.get((position + 1 + i) % tmp.size()));
        }

        return result;
    }
}
