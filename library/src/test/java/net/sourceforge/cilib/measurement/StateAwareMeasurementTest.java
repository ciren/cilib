/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
