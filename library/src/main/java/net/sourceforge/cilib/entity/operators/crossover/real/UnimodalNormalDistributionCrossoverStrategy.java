/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover.real;

import static com.google.common.base.Preconditions.checkState;
import com.google.common.collect.Lists;
import fj.P1;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
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
 * <p> Ono, I. & Kobayashi, S. A Real-coded Genetic
 * Algorithm for Function Optimization Using Unimodal Normal Distribution
 * Crossover. Proceedings of the Seventh International Conference on Genetic
 * Algorithms ICGA97 14, 246-253 (1997). </p>
 * 
 * <p> The code is based on the MOEA
 * Framework under the LGPL license: http://www.moeaframework.org </p>
 */
public class UnimodalNormalDistributionCrossoverStrategy implements CrossoverStrategy {

    private int numberOfParents;
    private int numberOfOffspring;
    private ControlParameter sigma1;
    private ControlParameter sigma2;
    private GaussianDistribution random;
    private boolean useIndividualProviders;

    /**
     * Default constructor.
     */
    public UnimodalNormalDistributionCrossoverStrategy() {
        this.numberOfParents = 3;
        this.numberOfOffspring = 1;
        this.sigma1 = ConstantControlParameter.of(0.1);
        this.sigma2 = ConstantControlParameter.of(0.1);
        this.random = new GaussianDistribution();
        this.useIndividualProviders = true;
    }

    /**
     * Copy constructor.
     *
     * @param copy
     */
    public UnimodalNormalDistributionCrossoverStrategy(UnimodalNormalDistributionCrossoverStrategy copy) {
        this.numberOfOffspring = copy.numberOfOffspring;
        this.sigma1 = copy.sigma1.getClone();
        this.sigma2 = copy.sigma2.getClone();
        this.random = copy.random;
        this.useIndividualProviders = copy.useIndividualProviders;
        this.numberOfParents = copy.numberOfParents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnimodalNormalDistributionCrossoverStrategy getClone() {
        return new UnimodalNormalDistributionCrossoverStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        checkState(parentCollection.size() >= 3, "There must be a minimum of three parents to perform UNDX crossover.");
        checkState(numberOfOffspring > 0, "At least one offspring must be generated. Check 'numberOfOffspring'.");

        List<Vector> solutions = Entities.<Vector>getCandidateSolutions(parentCollection);
        List<E> offspring = Lists.newArrayListWithCapacity(numberOfOffspring);
        UniformDistribution randomParent = new UniformDistribution();
        final int k = solutions.size();
        final int n = solutions.get(0).size();

        for (int os = 0; os < numberOfOffspring; os++) {
            //get index of main parent and put its solution at the end of the list
            int parent = (int) randomParent.getRandomNumber(0.0, k);
            Collections.swap(solutions, parent, k - 1);

            List<Vector> e_zeta = new ArrayList<Vector>();

            //calculate mean of parents except main parent
            Vector g = Vectors.mean(solutions.subList(0, k - 1));

            // basis vectors defined by parents
            for (int i = 0; i < k - 1; i++) {
                Vector d = solutions.get(i).subtract(g);

                if (!d.isZero()) {
                    double dbar = d.length();
                    Vector e = d.orthogonalize(e_zeta);

                    if (!e.isZero()) {
                        e_zeta.add(e.normalize().multiply(dbar));
                    }
                }
            }

            final double D = solutions.get(k - 1).subtract(g).length();

            // create the remaining basis vectors
            List<Vector> e_eta = Lists.newArrayList();
            e_eta.add(solutions.get(k - 1).subtract(g));

            for (int i = 0; i < n - e_zeta.size() - 1; i++) {
                Vector d = Vector.newBuilder().copyOf(g).buildRandom();
                e_eta.add(d);
            }

            e_eta = Vectors.orthonormalize(e_eta);

            // construct the offspring
            Vector variables = Vector.copyOf(g);

            if (!useIndividualProviders) {
                for (int i = 0; i < e_zeta.size(); i++) {
                    variables = variables.plus(e_zeta.get(i).multiply(random.getRandomNumber(0.0, sigma1.getParameter())));
                }

                for (int i = 0; i < e_eta.size(); i++) {
                    variables = variables.plus(e_eta.get(i).multiply(D * random.getRandomNumber(0.0, sigma2.getParameter() / Math.sqrt(n))));
                }
            } else {
                for (int i = 0; i < e_zeta.size(); i++) {
                    variables = variables.plus(e_zeta.get(i).multiply(new P1<Number>() {
                        @Override
                        public Number _1() {
                            return random.getRandomNumber(0.0, sigma1.getParameter());
                        }
                    }));
                }

                for (int i = 0; i < e_eta.size(); i++) {
                    variables = variables.plus(e_eta.get(i).multiply(new P1<Number>() {
                        @Override
                        public Number _1() {
                            return D * random.getRandomNumber(0.0, sigma2.getParameter() / Math.sqrt(n));
                        }
                    }));
                }
            }

            E child = (E) parentCollection.get(parent).getClone();
            child.setCandidateSolution(variables);
            
            offspring.add(child);
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
    
    public void setCrossoverPointProbability(double crossoverPointProbability) {
        throw new UnsupportedOperationException("Not applicable");
    }
    
    public ControlParameter getCrossoverPointProbability() {
        throw new UnsupportedOperationException("Not applicable");
    }
}
