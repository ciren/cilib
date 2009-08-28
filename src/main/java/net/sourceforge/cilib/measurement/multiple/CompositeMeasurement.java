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
package net.sourceforge.cilib.measurement.multiple;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * Measurement to perform measurements on a set of contained {@code Algorithm}
 * instances. This type of measurement is generally only defined for
 * {@link net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm}.
 */
public class CompositeMeasurement implements Measurement<TypeList> {
    private static final long serialVersionUID = -7109719897119621328L;
    private List<Measurement<? extends Type>> measurements;

    /**
     * Create a new instance with zero measurements.
     */
    public CompositeMeasurement() {
        this.measurements = new ArrayList<Measurement<? extends Type>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Measurement getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return "T";
    }

    /**
     * Get the measurement values for all sub-algorithms.
     * @param algorithm The top level algorithm
     * @return The values of measurements applied to all contained algorithms.
     */
    @Override
    public TypeList getValue(Algorithm algorithm) {
        TypeList vector = new TypeList();

        MultiPopulationBasedAlgorithm multi = (MultiPopulationBasedAlgorithm) algorithm;

        for (PopulationBasedAlgorithm single : multi) {
            for (Measurement<? extends Type> measurement : measurements) {
                vector.add(measurement.getValue(single));
            }
        }

        return vector;
    }

    /**
     * Add a measurement to the composite for evaluation on the sub-algorithms.
     * @param measurement The measurement to add.
     */
    public void addMeasurement(Measurement<? extends Type> measurement) {
        this.measurements.add(measurement);
    }

}
