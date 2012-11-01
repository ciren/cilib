/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @param <E>
 */
public class ConstantInitializationStrategy<E extends Entity> implements InitializationStrategy<E> {
    private static final long serialVersionUID = 4198258321374130337L;

    private double constant;

    public ConstantInitializationStrategy() {
        this.constant = 0.0;
    }

    public ConstantInitializationStrategy(double value) {
        this.constant = value;
    }

    public ConstantInitializationStrategy(ConstantInitializationStrategy copy) {
        this.constant = copy.constant;
    }

    @Override
    public ConstantInitializationStrategy getClone() {
        return new ConstantInitializationStrategy(this);
    }

    @Override
    public void initialize(Enum<?> key, E entity) {
        Type type = entity.getProperties().get(key);

        if (type instanceof Vector) {
            Vector vector = (Vector) type;

            for (int i = 0; i < vector.size(); i++) {
                vector.setReal(i, constant);
            }

            return;
        }

        throw new UnsupportedOperationException("Cannot perfrom initialization on non Vector type.");
    }

    public double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

}
