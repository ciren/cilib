/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover;

import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.Operator;

public interface CrossoverStrategy extends Operator {
    @Override
    public CrossoverStrategy getClone();

    public <E extends Entity> List<E> crossover(List<E> parentCollection);

    public int getNumberOfParents();

    public void setCrossoverPointProbability(double crossoverPointProbability);

    public ControlParameter getCrossoverPointProbability();
}
