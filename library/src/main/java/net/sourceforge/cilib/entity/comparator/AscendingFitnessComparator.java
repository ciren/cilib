/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.comparator;

import java.io.Serializable;
import java.util.Comparator;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.Fitness;

/**
 * Comparator to order {@linkplain Entity} instances based on fitness values.
 * This comparator results in a ascending order. This ordering is effectively
 * an ordering of entity instances from the "least fit" to the "most fit".
 *
 * @param <E> The {@code Entity} type.
 */
public class AscendingFitnessComparator<E extends Entity> implements Comparator<E>, Serializable {
    private static final long serialVersionUID = -4303050310093446507L;

    /**
     * Compare the {@linkplain Entity} objects returning the desired ordering.
     * @param e1 The first {@linkplain Entity} to be used in the comparison.
     * @param e2 The second {@linkplain Entity} to be used in the comparison.
     * @return -1 if e1 is less than e2;
     *         0 if e1 and e2 are equal
     *         1 if e2 is greater than e1
     */
    @Override
    public int compare(E e1, E e2) {
        Fitness f1 = e1.getFitness();
        Fitness f2 = e2.getFitness();

        return f1.compareTo(f2);
    }

}
