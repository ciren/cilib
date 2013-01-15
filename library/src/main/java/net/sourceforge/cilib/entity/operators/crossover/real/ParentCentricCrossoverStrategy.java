/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover.real;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import fj.P1;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.parentprovider.ParentProvider;
import net.sourceforge.cilib.entity.operators.crossover.parentprovider.RandomParentProvider;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Entities;
import net.sourceforge.cilib.util.Vectors;


/**
 * <p> Parent Centric Crossover Strategy </p>
 *
 * <p> References: </p>
 *
 * <p> Deb, K.; Joshi, D.; Anand, A.; , "Real-coded evolutionary algorithms with
 * parent-centric recombination," Evolutionary Computation, 2002. CEC '02.
 * Proceedings of the 2002 Congress on , vol.1, no., pp.61-66, 12-17 May 2002
 * doi: 10.1109/CEC.2002.1006210 </p> <p> The code is based on the MOEA
 * Framework under the LGPL license: http://www.moeaframework.org </p>
 */
public class ParentCentricCrossoverStrategy implements CrossoverStrategy {

    private int numberOfOffspring;
    private ControlParameter sigma1;
    private ControlParameter sigma2;
    private GaussianDistribution random;
    private boolean useIndividualProviders;
    private int numberOfParents;
    private ParentProvider parentProvider;

    public ParentCentricCrossoverStrategy() {
        this.numberOfOffspring = 1;
        this.sigma1 = ConstantControlParameter.of(0.1);
        this.sigma2 = ConstantControlParameter.of(0.1);
        this.random = new GaussianDistribution();
        this.useIndividualProviders = true;
        this.numberOfParents = 3;
        this.parentProvider = new RandomParentProvider();
    }

    public ParentCentricCrossoverStrategy(ParentCentricCrossoverStrategy copy) {
        this.numberOfOffspring = copy.numberOfOffspring;
        this.sigma1 = copy.sigma1.getClone();
        this.sigma2 = copy.sigma2.getClone();
        this.random = copy.random;
        this.useIndividualProviders = copy.useIndividualProviders;
        this.numberOfParents = copy.numberOfParents;
        this.parentProvider = copy.parentProvider.getClone();
    }

    @Override
    public CrossoverStrategy getClone() {
        return new ParentCentricCrossoverStrategy(this);
    }

    /**
     * Performs the parent centric crossover strategy<br/> Note: The selected
     * parent is placed at the end of the parent list
     *
     * @param parentCollection List of parents to use to perform crossover
     * @return List of offspring calculated using this crossover strategy
     */
    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() >= 2, "ParentCentricCrossoverStrategy requires at least 2 parents.");
        Preconditions.checkState(numberOfOffspring > 0, "At least one offspring must be generated. Check 'numberOfOffspring'.");

        List<Vector> solutions = Entities.<Vector>getCandidateSolutions(parentCollection);
        UniformDistribution randomParent = new UniformDistribution();
        List<E> offspring = Lists.newArrayList();
        int k = solutions.size();

        //calculate mean of parents
        Vector g = Vectors.mean(solutions);

        //get each offspring
        for (int os = 0; os < numberOfOffspring; os++) {
            // to allow the same parent to be selected by the parentProvider
            solutions = Entities.<Vector>getCandidateSolutions(parentCollection);
            int parent = parentCollection.indexOf(parentProvider.f((List<Entity>) parentCollection));
            Collections.swap(solutions, parent, k - 1);

            List<Vector> e_eta = new ArrayList<Vector>();
            e_eta.add(solutions.get(k - 1).subtract(g));

            double D = 0.0;

            // basis vectors defined by parents
            for (int i = 0; i < k - 1; i++) {
                Vector d = solutions.get(i).subtract(g);

                if (!d.isZero()) {
                    Vector e = d.orthogonalize(e_eta);

                    if (!e.isZero()) {
                        D += e.length();
                        e_eta.add(e.normalize());
                    }
                }
            }

            D /= k - 1;

            // construct the offspring
            Vector child = Vector.copyOf(solutions.get(k - 1));

            if (useIndividualProviders) {
                child = child.plus(e_eta.get(0).multiply(new P1<Number>() {
                    @Override
                    public Number _1() {
                        return random.getRandomNumber(0.0, sigma1.getParameter());
                    }
                }));

                for (int i = 1; i < e_eta.size(); i++) {
                    child = child.plus(e_eta.get(i).multiply(D).multiply(new P1<Number>() {

                        @Override
                        public Number _1() {
                            return random.getRandomNumber(0.0, sigma2.getParameter());
                        }
                    }));
                }
            } else {
                child = child.plus(e_eta.get(0).multiply(random.getRandomNumber(0.0, sigma1.getParameter())));

                double eta = random.getRandomNumber(0.0, this.sigma2.getParameter());
                for (int i = 1; i < e_eta.size(); i++) {
                    child = child.plus(e_eta.get(i).multiply(eta * D));
                }
            }

            E result = (E) parentCollection.get(parent).getClone();
            result.setCandidateSolution(child);

            offspring.add(result);
        }

        return offspring;
    }

    /**
     * Sets the deviation for the first Gaussian number
     *
     * @param dev The deviation to use
     */
    public void setSigma1(ControlParameter dev) {
        this.sigma1 = dev;
    }

    /**
     * Sets the deviation for the second Gaussian number
     *
     * @param dev The deviation to use
     */
    public void setSigma2(ControlParameter dev) {
        this.sigma2 = dev;
    }

    /**
     * Sets the number of offspring to calculate
     *
     * @param numberOfOffspring The number of offspring required
     */
    public void setNumberOfOffspring(int numberOfOffspring) {
        this.numberOfOffspring = numberOfOffspring;
    }

    /**
     * Sets whether to use different random numbers for different dimensions.
     *
     * @param useIndividualProviders
     */
    public void setUseIndividualProviders(boolean useIndividualProviders) {
        this.useIndividualProviders = useIndividualProviders;
    }

    @Override
    public int getNumberOfParents() {
        return numberOfParents;
    }

    public void setNumberOfParents(int numberOfParents) {
        this.numberOfParents = numberOfParents;
    }

    public ParentProvider getParentProvider() {
        return parentProvider;
    }

    public void setParentProvider(ParentProvider parentProvider) {
        this.parentProvider = parentProvider;
    }

    public void setCrossoverPointProbability(double crossoverPointProbability) {
        throw new UnsupportedOperationException("Not applicable");
    }

    public ControlParameter getCrossoverPointProbability() {
        throw new UnsupportedOperationException("Not applicable");
    }

}
