/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import net.sourceforge.cilib.type.types.Type;

/**
 * This class serves as the base class for all state based measurements. If a
 * measurement is a complex structure that needs to be computed over a period
 * of time (iterations / function evaluations etc), then the measurement should
 * extend this class.
 * <p>
 * The {@code MeasurementSuite} is aware of instances of this class and will
 * correctly handle any data recording and restoring that may be required as the
 * measurements are performed on the current algorithm.
 *
 * @param <E> The {@code Type} type.
 */
public abstract class StateAwareMeasurement<E extends Type> implements Measurement<E>, Externalizable {

    private boolean stateAware;

    public StateAwareMeasurement() {
        this.stateAware = true;
    }

    /**
     * Set the state of the measurement. If this property is set, the measurement
     * will then have it's internal state saved and restored each an every time
     * a measurement is to take place.
     * @param state The state awareness value of this measurement class.
     */
    public void setStateAware(boolean state) {
        this.stateAware = state;
    }

    /**
     * Get the state awareness of the current measurement.
     * @return The state awareness of the measurement.
     */
    public boolean isStateAware() {
        return this.stateAware;
    }

    /**
     * Set the state of the {@code Measurement} based on the provided instance.
     * @param memento The instance containing the externalised state.
     * @throws java.io.IOException If an IO error occurs.
     * @throws java.lang.ClassNotFoundException If a specified class cannot be found.
     */
    public void setState(MeasurementMemento memento) throws IOException, ClassNotFoundException {
        if (memento.getData() == null) {
            return;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(memento.getData());
        ObjectInputStream ios = new ObjectInputStream(bais);
        this.readExternal(ios);
        ios.close();
    }

    /**
     * Obtain a new externalised state from the current measurement. This state is
     * maintained in a {@linkplain MeasurementMemento} and is returned as an
     * immutable object.
     * @return The externalised state within a {@linkplain MeasurementMemento}
     * @throws java.io.IOException If an IO error occurs.
     */
    public MeasurementMemento getState() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        this.writeExternal(oos);
        oos.close();

        MeasurementMemento memento = new MeasurementMemento();
        memento.setData(baos.toByteArray());
        return memento;
    }
}
