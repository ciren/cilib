/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

public abstract class ClusteringProblem extends AbstractProblem{
    private int numberOfClusters;

    public ClusteringProblem() {
        numberOfClusters = 1;
    }

    public ClusteringProblem(ClusteringProblem copy) {
        super(copy);
        numberOfClusters = copy.numberOfClusters;
    }

    @Override
    protected abstract Fitness calculateFitness(Type solution);

    /**
     * Accessor for the domain of the function.
     *
     * @return a {@link DomainRegistry} representing the function domain.
     */
    @Override
    public DomainRegistry getDomain() {
        if (domainRegistry.getDomainString() == null) {
            throw new IllegalStateException("Domain has not been defined. Please define domain for function optimisation.");
        }
        return domainRegistry;
    }

    public void setDimension(int dimension) {
        this.domainRegistry.setDomainString(domainRegistry.getDomainString()
            .substring(0, domainRegistry.getDomainString()
            .indexOf(")") + 1) + "^" + dimension);
    }

    public void setNumberOfClusters(int newAmount) {
        numberOfClusters = newAmount;
    }

    public int getNumberOfClusters() {
        return numberOfClusters;
    }

}
