/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.problem.boundaryconstraint;

import net.cilib.entity.Entity;
import net.cilib.type.types.Numeric;
import net.cilib.type.types.Type;
import net.cilib.type.types.Types;
import net.cilib.type.types.container.Vector;

/**
 * Reinitialise each element within the provided {@code Type} element if
 * it is no longer within the valid search space. Each element violating the
 * condition will be reinitialised within the domain of the problem (search space).
 *
 * @see Types#isInsideBounds(net.cilib.type.types.Type)
 */
public class PerElementReinitialisation extends ReinitialisationBoundary {
    private static final long serialVersionUID = 7080824227269710787L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void enforce(Entity entity) {
        try {
            enforce((Vector) entity.getCandidateSolution());
        }
        catch (ClassCastException cce) {
            enforce((Numeric) entity.getCandidateSolution());
        }
    }

    /**
     * This method only randomises those elements inside the given {@linkplain Type} object that are out of bounds.
     * NOTE: This method is recursive so that it can handle <tt>Vectors</tt> inside <tt>Vectors</tt>.
     * @param type the {@linkplain Type} object whose individual elements should be randomised if they are out of bounds
     */
    private void enforce(Vector vector) {
        for (Type element : vector) {
            try {
                enforce((Numeric) element);
            }
            catch (ClassCastException cce) {
                enforce((Vector) element);
            }
        }
    }

    private void enforce(Numeric numeric) {
        if (!Types.isInsideBounds(numeric)) {
            numeric.randomise();
        }
    }
}
