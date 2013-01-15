/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Randomisable;
import net.sourceforge.cilib.type.types.Type;

/**
 *
 * @param <E>
 */
public class RandomInitialisationStrategy<E extends Entity> implements InitialisationStrategy<E> {
    private static final long serialVersionUID = 5630272366805104400L;

    @Override
    public RandomInitialisationStrategy getClone() {
        return this;
    }

    @Override
    public void initialise(Enum<?> key, E entity) {
        Type type = entity.getProperties().get(key);

        if (type instanceof Randomisable) {
            Randomisable randomisable = (Randomisable) type;
            randomisable.randomise();
            return;
        }

        throw new UnsupportedOperationException("Cannot initialise a non Randomisable instance.");
    }

}
