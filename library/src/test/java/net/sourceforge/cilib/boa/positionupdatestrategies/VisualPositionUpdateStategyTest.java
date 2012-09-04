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
package net.sourceforge.cilib.boa.positionupdatestrategies;

import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.boa.ABC;
import net.sourceforge.cilib.boa.bee.HoneyBee;
import net.sourceforge.cilib.boa.bee.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VisualPositionUpdateStategyTest {

    private ABC abc;

    @Before
    public void setUp() throws Exception {
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setFunction(new Rastrigin());
        problem.setDomain("R(-5.0:5.0)^10");

        StoppingCondition condition = new MeasuredStoppingCondition(new Iterations(), new Maximum(), 2);

        abc = new ABC();
        ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
        initStrategy.setEntityNumber(10);
        WorkerBee bee = new WorkerBee();
        initStrategy.setEntityType(bee);
        abc.setInitialisationStrategy(initStrategy);
        abc.setWorkerBeePercentage(ConstantControlParameter.of(0.5));
        abc.setForageLimit(ConstantControlParameter.of(Integer.MAX_VALUE));
        abc.addStoppingCondition(condition);
        abc.setOptimisationProblem(problem);
        abc.performInitialisation();
    }

    @Test
    public void testUpdatePosition() {
        HoneyBee bee = abc.getWorkerBees().get(0);
        abc.performIteration();
        Fitness oldFitness = bee.getFitness().getClone();
        abc.performIteration();
        Vector currentPosition = bee.getPosition();
        Assert.assertEquals(10, currentPosition.size());
        for (int i = 0; i < currentPosition.size(); i++) {
            Assert.assertTrue(((Real) currentPosition.get(i)).doubleValue() != Double.NaN);
            Assert.assertTrue(!Double.isInfinite(((Real) currentPosition.get(i)).doubleValue()));
        }
        Fitness newFitness = bee.getFitness();
        Assert.assertTrue(newFitness.compareTo(oldFitness) >= 0);
    }
}
