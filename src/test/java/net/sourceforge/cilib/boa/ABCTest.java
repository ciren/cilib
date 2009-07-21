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
package net.sourceforge.cilib.boa;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import net.sourceforge.cilib.boa.bee.HoneyBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.functions.continuous.unconstrained.Ackley;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.Type;

import org.junit.Test;

public class ABCTest {

    @Test
    public void testPerformInitialisation() {
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setFunction(new Ackley());

        StoppingCondition condition = new MaximumIterations(1000);

        ABC abc = new ABC();
        abc.addStoppingCondition(condition);
        abc.setOptimisationProblem(problem);
        abc.setWorkerBeePercentage(new ConstantControlParameter(0.7));

        abc.initialise();
        assertEquals(abc.getTopology().size(), 100);
        assertEquals(abc.getWorkerBees().size(), 70);
        assertEquals(abc.getOnlookerBees().size(), 30);
        HashMap<Type, Type> map = new HashMap<Type, Type>();

        for (HoneyBee bee : abc.getTopology())
        {
            map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
        }
        for (HoneyBee bee : abc.getWorkerBees())
        {
            map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
        }
        for (HoneyBee bee : abc.getOnlookerBees())
        {
            map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
        }

        assertEquals(100, map.size());
    }

}
