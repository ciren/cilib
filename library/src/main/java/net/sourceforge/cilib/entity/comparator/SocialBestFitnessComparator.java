/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.comparator;

import java.io.Serializable;
import java.util.Comparator;
import net.sourceforge.cilib.entity.SocialEntity;
import net.sourceforge.cilib.problem.solution.Fitness;

/**
 * Compare two {@link SocialEntity} instances, based on the available social best
 * fitness.
 * @param <E> The {@code SocialEntity} type.
 */
public class SocialBestFitnessComparator<E extends SocialEntity> implements Comparator<E>, Serializable {
    private static final long serialVersionUID = 9035734190908368266L;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(E o1, E o2) {
        Fitness f1 = o1.getSocialFitness();
        Fitness f2 = o2.getSocialFitness();

        return f1.compareTo(f2);
    }

}
