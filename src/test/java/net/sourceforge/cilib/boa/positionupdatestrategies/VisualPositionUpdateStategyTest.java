/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.boa.positionupdatestrategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.boa.ABC;
import net.sourceforge.cilib.boa.bee.HoneyBee;
import net.sourceforge.cilib.boa.bee.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Before;
import org.junit.Test;

public class VisualPositionUpdateStategyTest {

    private ABC abc;

    @Before
    public void setUp() throws Exception {
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        ContinuousFunction func = new Rastrigin();
        func.setDomain("R(-5.0, 5.0)^10");
        problem.setFunction(func);

        StoppingCondition condition = new MaximumIterations(2);

        abc = new ABC();
        ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
        initStrategy.setEntityNumber(10);
        WorkerBee bee = new WorkerBee();
        initStrategy.setEntityType(bee);
        abc.setInitialisationStrategy(initStrategy);
        abc.setWorkerBeePercentage(new ConstantControlParameter(0.5));
        abc.setForageLimit(new ConstantControlParameter(Integer.MAX_VALUE));
        abc.addStoppingCondition(condition);
        abc.setOptimisationProblem(problem);
        abc.initialise();
    }

    @Test
    public void testUpdatePosition() {
        HoneyBee bee = abc.getWorkerBees().get(0);
        abc.performIteration();
        Fitness oldFitness = bee.getFitness().getClone();
        abc.performIteration();
        Vector currentPosition = bee.getPosition();
        assertEquals(10, currentPosition.size());
        for (int i = 0; i < currentPosition.size(); i++) {
            assertTrue(((Real) currentPosition.get(i)).getReal() != Double.NaN);
            assertTrue(!Double.isInfinite(((Real) currentPosition.get(i)).getReal()));
        }
        Fitness newFitness = bee.getFitness();
        assertTrue(newFitness.compareTo(oldFitness)  >= 0);
    }
}
