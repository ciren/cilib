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
package net.sourceforge.cilib.measurement;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.type.types.StringType;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class StateAwareMeasurementTest {

    @Test
    public void extractAndLoadData() throws IOException, ClassNotFoundException {
        String expected = "this is a test string";

        MockStateAwareMeasurement measurement1 = new MockStateAwareMeasurement();
        measurement1.setValue(expected);

        MeasurementMemento memento = measurement1.getState();

        MockStateAwareMeasurement measurement2 = new MockStateAwareMeasurement();
        measurement2.setState(memento);

        Assert.assertEquals(expected, measurement2.getValue());
    }

    private class MockStateAwareMeasurement extends StateAwareMeasurement<StringType> {
        private static final long serialVersionUID = 6211593968000330634L;
        private String value;

        @Override
        public Measurement getClone() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getDomain() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public StringType getValue(Algorithm algorithm) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeUTF(value);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.value = in.readUTF();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
