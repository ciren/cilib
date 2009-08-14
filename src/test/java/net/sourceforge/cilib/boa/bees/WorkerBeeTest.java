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

import com.google.common.collect.Iterables;
import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.boa.bee.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;

import org.junit.Test;

public class WorkerBeeTest {

    @Test
    public void testSetForageLimit() {
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        ContinuousFunction func = new Rastrigin();
        func.setDomain("R(-10.048, 10.048)^100");
        problem.setFunction(func);

        ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
        initStrategy.setEntityNumber(100);
        WorkerBee clone = new WorkerBee();
        clone.setForageLimit(new ConstantControlParameter(680));
        initStrategy.setEntityType(clone);
        Topology<WorkerBee> population = new GBestTopology<WorkerBee>();
        Iterables.addAll(population, initStrategy.initialise(problem));

        for (WorkerBee bee : population) {
            assertEquals(bee.getForageLimit().getParameter(), 680.0, 0.0001);
        }
    }

}
