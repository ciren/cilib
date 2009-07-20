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
package net.sourceforge.cilib.boa.bees;



import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.boa.ABC;
import net.sourceforge.cilib.boa.bee.ExplorerBee;
import net.sourceforge.cilib.boa.bee.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExplorerBeeTest {
    private ABC abc;
    @Before
    public void setUp() throws Exception {
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        ContinuousFunction func = new Rastrigin();
        func.setDomain("R(-5.0, 5.0)^5");
        problem.setFunction(func);

        StoppingCondition condition = new MaximumIterations(2);

        abc = new ABC();
        ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
        initStrategy.setEntityNumber(10);
        WorkerBee bee = new WorkerBee();
        initStrategy.setEntityType(bee);
        abc.setForageLimit(new ConstantControlParameter(-1));
        abc.setInitialisationStrategy(initStrategy);
        abc.setWorkerBeePercentage(new ConstantControlParameter(0.5));
        abc.addStoppingCondition(condition);
        abc.setOptimisationProblem(problem);
        abc.initialise();
    }

    @Test
    public void testSearchAllowed() {
        //get up a position with bounds
        Vector oldPosition = abc.getWorkerBees().get(0).getPosition().getClone();;
        //update position with explorer bee
        ExplorerBee explorerBee = abc.getExplorerBee();
        Assert.assertTrue(explorerBee.searchAllowed(1));
        explorerBee.getNewPosition(1,oldPosition);
        //only one update is allowed for the same iteration, this must therefore be false...
        Assert.assertTrue(!explorerBee.searchAllowed(1));
        //and this true.
        Assert.assertTrue(explorerBee.searchAllowed(2));
    }

    @Test
    public void testGetNewPosition() {
        //get up a position with bounds
        Vector oldPosition = abc.getWorkerBees().get(0).getPosition().getClone();;
        //update position with explorer bee
        ExplorerBee explorerBee = abc.getExplorerBee();
        Vector newPosition = explorerBee.getNewPosition(1,oldPosition);
        Assert.assertTrue(!oldPosition.equals(newPosition));
    }

}
