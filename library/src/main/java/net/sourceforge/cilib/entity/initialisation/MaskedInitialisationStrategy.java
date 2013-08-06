/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Initialises a vector property according to a mask. If a mask element has
 * NaN as its value, the corresponding element in the property vector is
 * randomised. Otherwise, the element is set to the value given by the mask.
 *
 * @param <E> The entity type.
 */
public class MaskedInitialisationStrategy<E extends Entity> implements
        InitialisationStrategy<E> {
    private Vector mask;

    public MaskedInitialisationStrategy() {
        this.mask = null;
    }

    public MaskedInitialisationStrategy(Vector mask) {
        this.mask = mask;
    }

    public MaskedInitialisationStrategy(MaskedInitialisationStrategy copy) {
        this.mask = copy.mask.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MaskedInitialisationStrategy getClone() {
        return new MaskedInitialisationStrategy<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Enum<?> key, E entity) {
        Type type = entity.getProperties().get(key);

        if (type instanceof Vector) {
            Vector vector = (Vector) type;

            for (int curElement = 0; curElement < mask.size(); ++curElement) {
                if (Double.isNaN(mask.doubleValueOf(curElement))) {
                    vector.get(curElement).randomise();
                }
                else {
                    vector.setReal(curElement, mask.doubleValueOf(curElement));
                }
            }
        }
        else {
            throw new UnsupportedOperationException("Cannot perform initialisation on non Vector type.");
        }
    }

    public void setMask(Vector mask) {
        this.mask = mask;
    }
}
