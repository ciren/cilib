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
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;

/**
 *
 * @author Kristina
 */
public class QuantizationErrorMinimizationProblem extends OptimisationProblemAdapter{

    private DomainRegistry domainRegistry;
    
    public QuantizationErrorMinimizationProblem() {
        domainRegistry = new StringBasedDomainRegistry();
    }
    
    public QuantizationErrorMinimizationProblem(QuantizationErrorMinimizationProblem copy) {
        domainRegistry = copy.domainRegistry;
    }
    
    @Override
    public OptimisationProblemAdapter getClone() {
        return new QuantizationErrorMinimizationProblem(this);
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        CentroidHolder candidateSolution = (CentroidHolder) solution;
        double quantizationError = 0;
        
        for(ClusterCentroid centroid : (CentroidHolder) candidateSolution) {
            for(double distance : centroid.getDataItemDistances()) {
                quantizationError += distance / (double) centroid.getDataItemDistances().length;
            }
        }
        
        quantizationError /= (double) candidateSolution.size();
        
        return new MinimisationFitness(quantizationError);
    }
    
    /**
     * Accessor for the domain of the function. See {@link net.sourceforge.cilib.Domain.Component}.
     * @return The function domain.
     */
    @Override
    public DomainRegistry getDomain() {
        if (domainRegistry.getDomainString() == null) {
            throw new IllegalStateException("Domain has not been defined. Please define domain for function optimization.");
        }
        return domainRegistry;
    }

    /**
     * Sets the domain of the function.
     * @param representation the string representation for the function domain.
     */
    public void setDomain(String representation) {
        this.domainRegistry.setDomainString(representation);
    }
    
    public void setDimension(int dimension) {
        this.domainRegistry.setDomainString(domainRegistry.getDomainString() + "^" + dimension);
    }
    
}
