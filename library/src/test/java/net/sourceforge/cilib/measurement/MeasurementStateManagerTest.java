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
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Type;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class MeasurementStateManagerTest {

    @Test
    public void setState() throws ClassNotFoundException, IOException {
        PSO pso = new PSO();

        MockStateAwareMeasurement measurement = new MockStateAwareMeasurement();
        MeasurementStateManager manager = new MeasurementStateManager();
        manager.setState(pso, measurement);

        MeasurementMemento state = manager.getState(pso, measurement);
        Assert.assertNotNull(state);
    }


    private class MockStateAwareMeasurement extends StateAwareMeasurement {

        @Override
        public Measurement getClone() {
            return this;
        }

        @Override
        public Type getValue(Algorithm algorithm) {
            return null;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        }

    }
}
