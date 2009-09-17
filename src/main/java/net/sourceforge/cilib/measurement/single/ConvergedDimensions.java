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
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Calculates the average number of dimensions over all
 * particles that have converged within a specified error
 * from the specified optimum. This measurement will be
 * useful to see if convergence in certain dimensions is
 * too slow.
 *
 * @author  Andries Engelbrecht
 */
public class ConvergedDimensions implements Measurement<Real> {
    private static final long serialVersionUID = 7322191932976445577L;

    private ControlParameter errorThreshold;
    private Vector targetSolution;

    /** Creates a new instance of ConvergedDimensions. */
    public ConvergedDimensions() {
        errorThreshold = new ConstantControlParameter();
        errorThreshold.setParameter(0.0);
    }

    /**
     * Copy constructor. Creates a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public ConvergedDimensions(ConvergedDimensions copy) {
        this.errorThreshold = copy.errorThreshold.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConvergedDimensions getClone() {
        return new ConvergedDimensions(this);
    }

    /**
     * @return The solution error threshold.
     */
    public ControlParameter getErrorThreshold() {
        return this.errorThreshold;
    }

    /**
     * @return The target solution.
     */
    public StructuredType getTargetSolution() {
        return this.targetSolution;
    }

    /**
     * Set the error threshold
     * @param error
     */
    public void setErrorThreshold(double error) {
        this.errorThreshold.setParameter(error);
    }

    /**
     * Set the target solution string representation
     * @param stringRepresentation
     */
    public void setTargetSolution(String stringRepresentation) {
        this.targetSolution = (Vector) DomainParser.parse(stringRepresentation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return "R";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm;
        int populationSize = populationBasedAlgorithm.getTopology().size();

        int dimensions = 0;
        int numberConvergedDimensions = 0;
        double sumOfAverageConvergedDimensions = 0.0;

        for (Entity populationEntity : populationBasedAlgorithm.getTopology()) {
            dimensions = populationEntity.getDimension();

            int dimension = 0;
            numberConvergedDimensions = 0;
            for (Numeric position : (Vector) populationEntity.getCandidateSolution()) {
                double lowerBound = targetSolution.getReal(dimension) - this.errorThreshold.getParameter();
                double upperBound = targetSolution.getReal(dimension) + this.errorThreshold.getParameter();
                double value = position.getReal();

                if ((value >= lowerBound) && (value <= upperBound))
                    numberConvergedDimensions++;
                dimension++;
            }
            sumOfAverageConvergedDimensions += (double)numberConvergedDimensions / (double)dimensions;
        }

        return new Real(sumOfAverageConvergedDimensions / (double)populationSize * (double)dimensions);
    }

}
