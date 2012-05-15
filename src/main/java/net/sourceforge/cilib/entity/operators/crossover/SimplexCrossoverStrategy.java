/**
 * Computational Intelligence Library (CIlib) Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP) Department of Computer
 * Science University of Pretoria South Africa
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.entity.operators.crossover;

import static com.google.common.base.Preconditions.checkState;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Entities;
import net.sourceforge.cilib.util.Vectors;

/**
 * <p> Simplex Crossover Strategy </p>
 *
 * <p> References: </p> * <p> Tsutsui, S.; Goldberg, D.E.; , "Simplex crossover
 * and linkage identification: single-stage evolution vs. multi-stage
 * evolution," Evolutionary Computation, 2002. CEC '02. Proceedings of the 2002
 * Congress on, vol.1, no., pp.974-979, 12-17 May 2002 doi:
 * 10.1109/CEC.2002.1007057 URL:
 * http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=1007057&isnumber=21693
 * </p> <p> The code is based on the MOEA Framework under the LGPL license:
 * http://www.moeaframework.org </p>
 */
public class SimplexCrossoverStrategy extends CrossoverStrategy {

    private int numberOfOffspring;
    private ControlParameter epsilon;
    private boolean useDefaultEpsilon;

    /**
     * Default constructor.
     */
    public SimplexCrossoverStrategy() {
        this.numberOfOffspring = 1;
        this.epsilon = ConstantControlParameter.of(0.1);
        this.useDefaultEpsilon = true;
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
    public List<? extends Entity> crossover(List<? extends Entity> parentCollection) {
        checkState(parentCollection.size() >= 3, "There must be a minimum of three parents to perform UNDX crossover.");
        checkState(numberOfOffspring > 0, "At least one offspring must be generated. Check 'numberOfOffspring'.");

        List<Vector> solutions = Entities.<Vector>getCandidateSolutions(parentCollection);
        List<Vector> simplexVertices = Lists.newArrayList();
        List<Entity> offspring = new ArrayList<Entity>();
        MersenneTwister random = new MersenneTwister();
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
                        .multiply(Math.pow(random.nextDouble(), 1.0 / i)));
            }

            Vector variables = simplexVertices.get(n - 1).plus(offsetVectors.get(n - 1));

            Entity child = parentCollection.get(n - 1).getClone();
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

    /**
     * Sets whether to use different random numbers for different dimensions.
     *
     * @param useIndividualProviders
     */
    public void setUseIndividualProviders(boolean useIndividualProviders) {
        this.useDefaultEpsilon = useIndividualProviders;
    }
}
