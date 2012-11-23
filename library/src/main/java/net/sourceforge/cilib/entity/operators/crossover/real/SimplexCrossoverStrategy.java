/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover.real;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Entities;
import net.sourceforge.cilib.util.Vectors;

/**
 * <p> Simplex Crossover Strategy </p>
 *
 * <p> References: </p> 
 * 
 * <p> Tsutsui, S.; Goldberg, D.E.; , "Simplex crossover
 * and linkage identification: single-stage evolution vs. multi-stage
 * evolution," Evolutionary Computation, 2002. CEC '02. Proceedings of the 2002
 * Congress on, vol.1, no., pp.974-979, 12-17 May 2002 doi:
 * 10.1109/CEC.2002.1007057 URL:
 * http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=1007057&isnumber=21693
 * </p> 
 * 
 * <p> The code is based on the MOEA Framework under the LGPL license:
 * http://www.moeaframework.org </p>
 */
public class SimplexCrossoverStrategy implements CrossoverStrategy {

    private int numberOfOffspring;
    private ControlParameter epsilon;
    private boolean useDefaultEpsilon;
    private int numberOfParents;

    /**
     * Default constructor.
     */
    public SimplexCrossoverStrategy() {
        this.numberOfOffspring = 1;
        this.epsilon = ConstantControlParameter.of(0.1);
        this.useDefaultEpsilon = true;
        this.numberOfParents = 3;
    }

    /**
     * Copy constructor.
     *
     * @param copy
     */
    public SimplexCrossoverStrategy(SimplexCrossoverStrategy copy) {
        this.numberOfOffspring = copy.numberOfOffspring;
        this.epsilon = copy.epsilon.getClone();
        this.useDefaultEpsilon = copy.useDefaultEpsilon;
        this.numberOfParents = copy.numberOfParents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimplexCrossoverStrategy getClone() {
        return new SimplexCrossoverStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() >= 3, "ParentCentricCrossoverStrategy requires at least 3 parents.");
        Preconditions.checkState(numberOfOffspring > 0, "At least one offspring must be generated. Check 'numberOfOffspring'.");

        List<Vector> solutions = Entities.<Vector>getCandidateSolutions(parentCollection);
        List<Vector> simplexVertices = Lists.newArrayList();
        List<E> offspring = Lists.newArrayList();
        ProbabilityDistributionFunction random = new UniformDistribution();
        Vector mean = Vectors.mean(solutions);
        final int n = solutions.size();

        if (useDefaultEpsilon) {
            epsilon = ConstantControlParameter.of(n + 1);
        }

        // compute simplex vertices expanded by epsilon
        for (Vector v : solutions) {
            simplexVertices.add(mean.plus(v.subtract(mean).multiply(epsilon.getParameter())));
        }

        for (int os = 0; os < numberOfOffspring; os++) {
            // calculate offset vectors
            List<Vector> offsetVectors = Lists.newArrayList();
            offsetVectors.add(mean.subtract(mean)); // add a zero vector (using mean - mean to preserve bounds)

            for (int i = 1; i < n; i++) {
                offsetVectors.add(simplexVertices.get(i - 1)
                        .subtract(simplexVertices.get(i))
                        .plus(offsetVectors.get(i - 1))
                        .multiply(Math.pow(random.getRandomNumber(), 1.0 / i)));
            }

            Vector variables = simplexVertices.get(n - 1).plus(offsetVectors.get(n - 1));

            E child = (E) parentCollection.get(n - 1).getClone();
            child.setCandidateSolution(variables);
            
            offspring.add(child);
        }

        return offspring;
    }

    /**
     * Sets the expansion rate.
     *
     * @param epsilon The new expansion rate.
     */
    public void setEpsilon(ControlParameter epsilon) {
        this.epsilon = epsilon;
        this.useDefaultEpsilon = false;
    }

    /**
     * Gets the expansion rate.
     *
     * @return The expansion rate.
     */
    public ControlParameter getEpsilon() {
        return epsilon;
    }

    /**
     * Sets the number of offspring to calculate
     *
     * @param numberOfOffspring The number of offspring required
     */
    public void setNumberOfOffspring(int numberOfOffspring) {
        this.numberOfOffspring = numberOfOffspring;
    }

    public int getNumberOfOffspring() {
        return numberOfOffspring;
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
