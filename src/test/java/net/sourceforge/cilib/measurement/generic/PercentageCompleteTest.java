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
package net.sourceforge.cilib.measurement.generic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

/**
 *
 * @author Gary Pampara
 */
@RunWith(JMock.class)
public class PercentageCompleteTest {
    private Mockery context = new JUnit4Mockery();

    @Test
    public void value() {
        PSO pso = new PSO();
        MaximumIterations maximumIterations = new MaximumIterations(100);
        maximumIterations.setAlgorithm(pso);
        pso.addStoppingCondition(maximumIterations);

        for (int i = 0; i < 10; i++)
            pso.performIteration();

        PercentageComplete percentageComplete = new PercentageComplete();
        Assert.assertEquals(0.1, percentageComplete.getValue(pso).getReal(), 0.001);
    }

    @Test
    public void resultType() {
//        final Algorithm algorithm = context.mock(Algorithm.class);
//
//        context.checking(new Expectations() {{
////            oneOf(algorithm).
//        }});
//
//        PercentageComplete percentageComplete = new PercentageComplete();
//        Assert.assertThat(percentageComplete.getValue(pso), is(Real.class));
    }

    @Test
    public void testPercentageCompleteDomain() {
        Measurement m = new PercentageComplete();

        Vector vector = (Vector) DomainParser.parse(m.getDomain());

        assertEquals(1, vector.getDimension());
        assertTrue(vector.get(0) instanceof Real);

        Real r = (Real) vector.get(0);
        assertEquals(0.0, r.getBounds().getLowerBound(), Double.MIN_NORMAL);
        assertEquals(1.0, r.getBounds().getUpperBound(), Double.MIN_NORMAL);
    }

}
