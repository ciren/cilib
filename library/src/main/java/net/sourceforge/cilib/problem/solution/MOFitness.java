/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.solution;

/**
 *
 */
public interface MOFitness extends Fitness, Iterable<Fitness> {

    public Fitness getFitness(int index);

    public int getDimension();

    public boolean dominates(MOFitness other);
}
