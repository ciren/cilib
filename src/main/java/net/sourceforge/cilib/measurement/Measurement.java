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
package net.sourceforge.cilib.measurement;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;


/**
 * All measurements must implement this interface. The measurment must return
 * the value of what it is measuring given the algorithm that it is measuring.
 *
 * @param <E> The return {@code Type}.
 * @author Edwin Peer
 * @author Gary Pampara
 */
public interface Measurement<E extends Type> extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    public Measurement getClone();

    /**
     * Get the domain string representing what this measurement's results will conform to.
     * @return The Domain String representing the represenation of the measurement
     */
    public String getDomain();


    /**
     * Get the value of the measurement. The represenation of the measurement will be based
     * on the domain string defined {@see Measurement#getDomain()}
     * @param algorithm The algorithm to obtain the measurement from.
     * @return The <tt>Type</tt> representing the value of the measurement.
     */
    public E getValue(Algorithm algorithm);
}
