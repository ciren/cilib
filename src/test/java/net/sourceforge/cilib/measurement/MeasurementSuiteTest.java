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
package net.sourceforge.cilib.measurement;

import java.io.IOException;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.simulator.MeasurementSuite;
import net.sourceforge.cilib.simulator.SynchronizedOutputBuffer;
import net.sourceforge.cilib.type.types.Type;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This test class tests the MeasurementSuite. I'm not sure how effective these
 * tests are though...
 *
 * @author gpampara
 */
@RunWith(JMock.class)
public class MeasurementSuiteTest {

    private Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    @Test
    public void measure() throws IOException, ClassNotFoundException {
        final Algorithm a = context.mock(Algorithm.class);
        final Measurement e = context.mock(Measurement.class);
        final SynchronizedOutputBuffer buffer = context.mock(SynchronizedOutputBuffer.class);

        MeasurementSuite s = new MeasurementSuite();

        context.checking(new Expectations() {{
            exactly(1).of(e).getValue(a);
            will(returnValue(with(Type.class)));

            exactly(1).of(buffer).writeMeasuredValue(with(any(Object.class)),
                with(any(Algorithm.class)),
                with(any(Measurement.class)));
        }});

        s.setOutputBuffer(buffer);
        s.addMeasurement(e);
        s.measure(a);
    }

}
