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

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;

public abstract class ClusteringProblem extends OptimisationProblemAdapter{
    private int numberOfClusters;
    private DomainRegistry domainRegistry;

    public ClusteringProblem() {
        domainRegistry = new StringBasedDomainRegistry();
        numberOfClusters = 1;
    }

    public ClusteringProblem(ClusteringProblem copy) {
        domainRegistry = copy.domainRegistry;
        numberOfClusters = copy.numberOfClusters;
    }

    @Override
    protected abstract Fitness calculateFitness(Type solution);

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
        this.domainRegistry.setDomainString(domainRegistry.getDomainString().substring(0, domainRegistry.getDomainString().indexOf(")") + 1) + "^" + dimension);
    }

    public void setNumberOfClusters(int newAmount) {
        numberOfClusters = newAmount;
    }

    public int getNumberOfClusters() {
        return numberOfClusters;
    }

}
