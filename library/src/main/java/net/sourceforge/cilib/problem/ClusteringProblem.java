/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

public abstract class ClusteringProblem extends AbstractProblem{
    private int numberOfClusters;
    private boolean dimensionSet;
    protected SlidingWindow window;

    public ClusteringProblem() {
        numberOfClusters = 1;
        dimensionSet = false;
    }

    public ClusteringProblem(ClusteringProblem copy) {
        super(copy);
        numberOfClusters = copy.numberOfClusters;
        dimensionSet = copy.dimensionSet;
        window = copy.window;
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
        if (!dimensionSet) {
            setDimension();
        }
        if (domainRegistry.getDomainString() == null) {
            throw new IllegalStateException("Domain has not been defined. Please define domain for function optimisation.");
        }
        return domainRegistry;
    }

    private void setDimension() {
        DataTable dataset = this.window.slideWindow();
        Vector pattern = ((StandardPattern) dataset.getRow(0)).getVector();
        int dimension = pattern.size();
        
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

    public void setWindow(SlidingWindow window) {
        this.window = window;
    }

    public SlidingWindow getWindow() {
        return this.window;
    }

}
