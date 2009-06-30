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
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.functions.continuous.Spherical;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.parser.ParseException;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Gary Pampara
 */
public class FitnessEvaluationsTest {

    @Test
    public void result() {
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setFunction(new Spherical());

        PSO pso = new PSO();
        pso.setOptimisationProblem(problem);
        pso.addStoppingCondition(new MaximumIterations(1));

        pso.initialise();
        pso.performIteration();

        Measurement m = new FitnessEvaluations();
        Assert.assertEquals(20, ((Int) m.getValue(pso)).getInt());

        pso.performIteration();
        Assert.assertEquals(40, ((Int) m.getValue(pso)).getInt());
    }

    @Test
    public void testFitnessEvaluationsDomain() throws ParseException {
        Measurement m = new FitnessEvaluations();

        Vector vector = (Vector) DomainParser.parse(m.getDomain());

        assertEquals(1, vector.getDimension());
        assertTrue(vector.get(0) instanceof Int);
    }

}
