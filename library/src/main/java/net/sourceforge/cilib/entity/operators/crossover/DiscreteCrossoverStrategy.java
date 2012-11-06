/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover;

import java.util.List;
import net.sourceforge.cilib.entity.Entity;

public interface DiscreteCrossoverStrategy extends CrossoverStrategy {

    @Override
    public DiscreteCrossoverStrategy getClone();

    public <E extends Entity> List<E> crossover(List<E> parentCollection, List<Integer> crossoverPoints);

    public List<Integer> getCrossoverPoints();

}
