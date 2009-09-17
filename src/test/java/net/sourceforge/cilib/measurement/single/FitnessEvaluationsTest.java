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

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 * @author Gary Pampara
 */
@RunWith(JMock.class)
public class FitnessEvaluationsTest {
    private Mockery context = new JUnit4Mockery();

    @Test
    public void result() {
        final PopulationBasedAlgorithm pba = context.mock(PopulationBasedAlgorithm.class);
        final OptimisationProblem problem = context.mock(OptimisationProblem.class);

        context.checking(new Expectations() {{
            exactly(2).of(pba).getOptimisationProblem(); will(returnValue(problem));
            exactly(2).of(problem).getFitnessEvaluations(); will(onConsecutiveCalls(returnValue(10), returnValue(20)));
        }});

        Measurement m = new FitnessEvaluations();
        Int i1 = (Int) m.getValue(pba);
        Int i2 = (Int) m.getValue(pba);

        Assert.assertThat(i1.getInt(), is(10));
        Assert.assertThat(i2.getInt(), is(20));
    }

    @Test
    public void testFitnessEvaluationsDomain() {
        Measurement m = new FitnessEvaluations();
        Vector vector = (Vector) DomainParser.parse(m.getDomain());

        assertEquals(1, vector.getDimension());
        assertTrue(vector.get(0) instanceof Int);
    }

}
