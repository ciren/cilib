/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.scoring;

public final class Score<E> implements Comparable<Score<E>> {
    private final E entity;
    private final int score;

    public Score(E entity, int score) {
        this.entity = entity;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public E getEntity() {
        return entity;
    }

    @Override
    public int compareTo(Score<E> o) {
        return Integer.compare(score, o.getScore());
    }
}
