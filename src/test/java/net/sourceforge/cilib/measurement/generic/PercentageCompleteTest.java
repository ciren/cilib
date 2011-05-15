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
package net.sourceforge.cilib.measurement.generic;

import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Gary Pampara
 */
public class PercentageCompleteTest {

    @Test
    public void value() {
        PSO pso = new PSO();
        MaximumIterations maximumIterations = new MaximumIterations(100);
        pso.addStoppingCondition(maximumIterations);

        for (int i = 0; i < 10; i++)
            pso.performIteration();

        PercentageComplete percentageComplete = new PercentageComplete();
        Assert.assertEquals(0.1, percentageComplete.getValue(pso).doubleValue(), 0.001);
    }

    @Test
    public void testPercentageCompleteDomain() {
        Measurement m = new PercentageComplete();

        Vector vector = (Vector) DomainParser.parse(m.getDomain());

        assertEquals(1, vector.size());
        assertTrue(vector.get(0) instanceof Real);

        Real r = (Real) vector.get(0);
        assertEquals(0.0, r.getBounds().getLowerBound(), Double.MIN_NORMAL);
        assertEquals(1.0, r.getBounds().getUpperBound(), Double.MIN_NORMAL);
    }

}
