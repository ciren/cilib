/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.scoring;

import net.sourceforge.cilib.entity.Entity;

public class SuccessCountScoringStrategy<E extends Entity> implements ScoringStrategy<E> {

    @Override
    public int getScore(E current, Iterable<E> opponents) {
        int score = 0;
        for (E i : opponents) {
            if (current.getFitness().compareTo(i.getFitness()) > 0) {
                score++;
            }
        }
        return score;
    }

}
