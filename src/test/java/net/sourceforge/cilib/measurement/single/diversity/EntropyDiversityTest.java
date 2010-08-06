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
package net.sourceforge.cilib.measurement.single.diversity;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Bennie Leonard
 */
public class EntropyDiversityTest {
    private PSO pso = new PSO();
    private Diversity diversityMeasure;
    private Real diversity;
    private int dimensions;

    @Before
    public void initialise() {
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^30");
        problem.setFunction(new Spherical());

        pso = new PSO();
        pso.setOptimisationProblem(problem);
        pso.addStoppingCondition(new MaximumIterations(1));
        
        pso.initialise();

        dimensions = pso.getOptimisationProblem().getDomain().getDimension();
    }

    @Test
    public void averageEntropyDiversityTest() {
        diversityMeasure = new AverageEntropyDiversityMeasure(30);

        forceConvergence(pso);
        diversity = diversityMeasure.getValue(pso);

        //diversity of converged swarm must be 0.0
        assertEquals(0.0, diversity.doubleValue(), 0.0);

        //move a single particle to the edge of the search space in one dimension
        Vector position = new Vector(30, Real.valueOf(0.0));
        position.setReal(0, -5.12);
        pso.getTopology().get(0).getProperties().put(EntityType.CANDIDATE_SOLUTION, position);

        //
        //the diversity of the swarm is taken as the average entropy over all
        //dimensions. In this case, the swarm is mostly converged at the origin,
        //with the exception of one particle at the edge of the search space in
        //one dimension.
        //This should give an entropy of about 0.058366281 in the particular
        //dimension as is shown below.
        //
        //Population size (pSize) = 20
        //intervals (i) = 30
        //P1 = 1/pSize = 0.05
        //P2 = 19/pSize = 0.95
        //
        //Entropy = -((P1 log_i P1) + (P2 log_i P2))
        //        = -((0.05 log_30 0.05) + (0.95 log_30 0.95))
        //        = 0.058366281
        //
        //The entropy in all other dimensions is 0.0, because the swarm is
        //converged. Therefore the average entropy over all dimensions is
        //(0.058366281 + 30 * 0.0) / 30 = 0.0019455427120674657
        //
        diversity = diversityMeasure.getValue(pso);
        assertEquals(0.0019455427120674657, diversity.doubleValue(), 0.0);
    }

    @Test
    public void maximumEntropyDiversityTest() {
        diversityMeasure = new MaximumEntropyDiversityMeasure(30);

        forceConvergence(pso);

        //diversity of converged swarm must be 0.0
        diversity = diversityMeasure.getValue(pso);
        assertEquals(0.0, diversity.doubleValue(), 0.0);

        //move a single particle to the edge of the search space in one dimension
        Vector position = new Vector(30, Real.valueOf(0.0));
        position.setReal(0, -5.12);
        pso.getTopology().get(0).getProperties().put(EntityType.CANDIDATE_SOLUTION, position);

        //
        //the diversity of the swarm is taken as the maximum entropy over all
        //dimensions. In this case, the swarm is mostly converged at the origin,
        //with the exception of one particle at the edge of the search space in
        //one dimension.
        //This should give an entropy of about 0.058366281 in the particular
        //dimension as is shown below.
        //
        //Population size (pSize) = 20
        //intervals (i) = 30
        //P1 = 1/pSize = 0.05
        //P2 = 19/pSize = 0.95
        //
        //Entropy = -((P1 log_i P1) + (P2 log_i P2))
        //        = -((0.05 log_30 0.05) + (0.95 log_30 0.95))
        //        = 0.05836628136202397
        //
        diversity = diversityMeasure.getValue(pso);
        assertEquals(0.05836628136202397, diversity.doubleValue(), 0.0);
    }

    /**
     * This method moves all particles to one point in the search space
     * to simulate convergence for testing purposes
     */
    private void forceConvergence(PSO algorithm) {
        Real position = Real.valueOf(0.0);

        for(Entity e : algorithm.getTopology()) {
            e.getProperties().put(EntityType.CANDIDATE_SOLUTION, new Vector(dimensions, position));
        }
    }
}
