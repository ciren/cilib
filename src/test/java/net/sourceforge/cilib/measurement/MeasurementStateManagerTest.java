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
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Type;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
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
        public String getDomain() {
            return null;
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
