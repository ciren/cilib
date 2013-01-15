/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.dynamic;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import net.sourceforge.cilib.measurement.StateAwareMeasurement;
import net.sourceforge.cilib.type.types.Type;

public abstract class DynamicMeasurement<E extends Type> extends StateAwareMeasurement<E> {

    protected double avg;

    public DynamicMeasurement() {
        setStateAware(true);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        this.avg = in.readDouble();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(avg);
    }
}
