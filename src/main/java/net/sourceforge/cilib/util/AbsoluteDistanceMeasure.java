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
package net.sourceforge.cilib.util;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * @deprecated Make use of {@link net.sourceforge.cilib.util.ManhattanDistance Manhattan Distance}. It is the correct name.
 * @author Edwin Peer
 */
@Deprecated
public class AbsoluteDistanceMeasure implements DistanceMeasure {

    /**
     * {@inheritDoc}
     */
    public <T extends Type, U extends StructuredType<T>> double distance(U x, U y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("Unmatched argument lengths");
        }

        Iterator<T> xIterator = x.iterator();
        Iterator<T> yIterator = y.iterator();

        double distance = 0;
        for (int i = 0; i < x.size(); ++i) {
            Numeric xNumeric = (Numeric) xIterator.next();
            Numeric yNumeric = (Numeric) yIterator.next();

            distance += Math.abs(xNumeric.getReal() - yNumeric.getReal());
        }

        return distance;
    }

    /**
     * {@inheritDoc}
     */
    public <T extends Collection<? extends Number>> double distance(T x, T y) {
        if (x.size() != y.size())
            throw new IllegalArgumentException("Unmatched argument lengths");

        double distance = 0;
        Iterator<? extends Number> i = x.iterator();
        Iterator<? extends Number> j = y.iterator();

        while (i.hasNext() && j.hasNext()) {
            Number n1 = i.next();
            Number n2 = j.next();

            double tmp = Math.abs(n1.doubleValue() - n2.doubleValue());

            distance += tmp;
        }

        return distance;
    }

}
