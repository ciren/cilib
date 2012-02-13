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
package net.sourceforge.cilib.entity.operators.crossover;

import static com.google.common.base.Preconditions.checkState;
import com.google.common.base.Supplier;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Entities;
import net.sourceforge.cilib.util.Vectors;

/**
 * <p>
 * Parent Centric Crossover Strategy
 * </p>
 * 
 * <p>
 * References:
 * </p> * 
 * <p>
 * Ono, I. & Kobayashi, S. A Real-coded Genetic Algorithm for Function Optimization Using Unimodal Normal Distribution Crossover. 
 * Proceedings of the Seventh International Conference on Genetic Algorithms ICGA97 14, 246-253 (1997).
 * </p>
 * <p>
 * The code is based on the MOEA Framework under the LGPL license: http://www.moeaframework.org
 * </p>
 * 
 * @author filipe
 */
public class UnimodalNormalDistributionCrossoverStrategy extends CrossoverStrategy {
    
    private int numberOfOffspring;
    private ControlParameter sigma1;
    private ControlParameter sigma2;
    private GaussianDistribution random;
    private boolean useIndividualProviders;
    
    /**
     * Default constructor.
     */
    public UnimodalNormalDistributionCrossoverStrategy() {
        this.numberOfOffspring = 1;
        this.sigma1 = new ConstantControlParameter(0.1);
        this.sigma2 = new ConstantControlParameter(0.1);
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
    public List<Entity> crossover(List<Entity> parentCollection) {
        List<Entity> offspring = new ArrayList<Entity>();
        List<Vector> parents = Entities.<Vector>getCandidateSolutions(parentCollection);
        
        final int k = parents.size();
        final int n = parents.get(0).size();

        //calculate mean of parents
        Vector g = Vectors.mean(parents);

        for (int os = 0; os < numberOfOffspring; os++) {
            checkState(parents.size() >= 3, "There must be a minimum of three parents to perform UNDX crossover.");
            checkState(numberOfOffspring > 0, "At least one offspring must be generated. Check 'numberOfOffspring'.");

            List<Vector> e_zeta = new ArrayList<Vector>();
            List<Vector> e_eta = new ArrayList<Vector>();

            // basis vectors defined by parents
            for (int i = 0; i < k - 1; i++) {
                Vector d = parents.get(i).subtract(g);

                if (!d.isZero()) {
                    double dbar = d.length();
                    Vector e = d.orthogonalize(e_zeta);

                    if (!e.isZero()) {
                        e_zeta.add(e.normalize().multiply(dbar));
                    }
                }
            }

            double D = parents.get(k - 1).subtract(g).length();

            // create the remaining basis vectors
            for (int i = 0; i < n - e_zeta.size(); i++) {
                Vector d = Vector.newBuilder().copyOf(g).buildRandom();

                if (!d.isZero()) {
                    Vector e = d.orthogonalize(e_eta);

                    if (!e.isZero()) {
                        e_eta.add(e.normalize().multiply(D));
                    }
                }
            }

            // construct the offspring
            Vector variables = Vector.copyOf(g);

            if (!useIndividualProviders) {
                for (int i = 0; i < e_zeta.size(); i++) {
                    variables = variables.plus(e_zeta.get(i).multiply(random.getRandomNumber(0.0, sigma1.getParameter())));
                }

                for (int i = 0; i < e_eta.size(); i++) {
                    variables = variables.plus(e_eta.get(i).multiply(random.getRandomNumber(0.0, sigma2.getParameter() / Math.sqrt(n))));
                }
            } else {
                for (int i = 0; i < e_zeta.size(); i++) {
                    variables = variables.plus(e_zeta.get(i).multiply(new Supplier<Number>() {
                        @Override
                        public Number get() {
                            return random.getRandomNumber(0.0, sigma1.getParameter());
                        }
                    }));
                }

                for (int i = 0; i < e_eta.size(); i++) {
                    variables = variables.plus(e_eta.get(i).multiply(new Supplier<Number>() {
                        @Override
                        public Number get() {
                            return random.getRandomNumber(0.0, sigma2.getParameter() / Math.sqrt(n));
                        }
                    }));
                }
            }

            Entity child = parentCollection.get(k - 1).getClone();
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
}
