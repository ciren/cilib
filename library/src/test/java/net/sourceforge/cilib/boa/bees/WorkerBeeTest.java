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
package net.sourceforge.cilib.boa.bees;

import com.google.common.collect.Iterables;
import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.boa.bee.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;

import org.junit.Test;

public class WorkerBeeTest {

    @Test
    public void testSetForageLimit() {
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setDomain("R(-10.048:10.048)^100");
        ContinuousFunction func = new Rastrigin();
        problem.setFunction(func);

        ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
        initStrategy.setEntityNumber(100);
        WorkerBee clone = new WorkerBee();
        clone.setForageLimit(ConstantControlParameter.of(680));
        initStrategy.setEntityType(clone);
        Topology<WorkerBee> population = new GBestTopology<WorkerBee>();
        Iterables.addAll(population, initStrategy.initialise(problem));

        for (WorkerBee bee : population) {
            assertEquals(bee.getForageLimit().getParameter(), 680.0, 0.0001);
        }
    }

}
