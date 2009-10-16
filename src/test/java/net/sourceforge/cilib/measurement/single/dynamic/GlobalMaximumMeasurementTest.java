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
package net.sourceforge.cilib.measurement.single.dynamic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.type.types.Real;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Julien Duhain
 */
@RunWith(JMock.class)
public class GlobalMaximumMeasurementTest {
    private Mockery mockery = new JUnit4Mockery()
    {{
       setImposteriser(ClassImposteriser.INSTANCE);
    }};

    @Test
    public void results() {
        final Algorithm algorithm = mockery.mock(Algorithm.class);
        final FunctionMaximisationProblem mockProblem = mockery.mock(FunctionMaximisationProblem.class);
        final ContinuousFunction mockFunction = mockery.mock(ContinuousFunction.class);
        mockery.checking(new Expectations() {{
            exactly(1).of(algorithm).getOptimisationProblem();will(returnValue(mockProblem));
            exactly(1).of(mockProblem).getFunction();will(returnValue(mockFunction));
            exactly(1).of(mockFunction).getMaximum();will(returnValue(10.0));
        }});

        Measurement m = new GlobalMaximum();
        Assert.assertEquals(10.0, ((Real) m.getValue(algorithm)).getReal(), 0.00001);
    }
}
