/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @param <E>
 */
public class ConstantInitialisationStrategy<E extends Entity> implements InitialisationStrategy<E> {
    private static final long serialVersionUID = 4198258321374130337L;

    private double constant;

    public ConstantInitialisationStrategy() {
        this.constant = 0.0;
    }

    public ConstantInitialisationStrategy(double value) {
        this.constant = value;
    }

    public ConstantInitialisationStrategy(ConstantInitialisationStrategy copy) {
        this.constant = copy.constant;
    }

    @Override
    public ConstantInitialisationStrategy getClone() {
        return new ConstantInitialisationStrategy(this);
    }

    @Override
    public void initialise(Enum<?> key, E entity) {
        Type type = entity.getProperties().get(key);

        if (type instanceof Vector) {
            Vector vector = (Vector) type;

            for (int i = 0; i < vector.size(); i++) {
                vector.setReal(i, constant);
            }

            return;
        }

        throw new UnsupportedOperationException("Cannot perform initialisation on non Vector type.");
    }

    public double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

}
