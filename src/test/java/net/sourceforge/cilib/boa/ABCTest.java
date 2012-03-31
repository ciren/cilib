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
package net.sourceforge.cilib.boa;

import java.util.HashMap;
import net.sourceforge.cilib.boa.bee.HoneyBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.functions.continuous.unconstrained.Ackley;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.Type;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ABCTest {

    @Test
    public void testPerformInitialisation() {
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-32.768, 32.768)^30");
        problem.setFunction(new Ackley());

        MeasuredStoppingCondition condition = new MeasuredStoppingCondition(new Iterations(), new Maximum(), 100);

        ABC abc = new ABC();
        abc.addStoppingCondition(condition);
        abc.setOptimisationProblem(problem);
        abc.setWorkerBeePercentage(ConstantControlParameter.of(0.7));

        abc.initialise();
        assertEquals(abc.getTopology().size(), 100);
        assertEquals(abc.getWorkerBees().size(), 70);
        assertEquals(abc.getOnlookerBees().size(), 30);
        HashMap<Type, Type> map = new HashMap<Type, Type>();

        for (HoneyBee bee : abc.getTopology()) {
            map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
        }
        for (HoneyBee bee : abc.getWorkerBees()) {
            map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
        }
        for (HoneyBee bee : abc.getOnlookerBees()) {
            map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
        }

        assertEquals(100, map.size());
    }
}
