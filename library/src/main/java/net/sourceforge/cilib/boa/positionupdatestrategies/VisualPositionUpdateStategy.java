/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.boa.positionupdatestrategies;

import net.sourceforge.cilib.boa.bee.HoneyBee;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Represents the visual exploration strategy used by bees to choose a their next
 * forage patch close by.
 *
 */
public class VisualPositionUpdateStategy implements BeePositionUpdateStrategy {

    private static final long serialVersionUID = 3782171955167557793L;

    /**
     * {@inheritDoc}
     */
    @Override
    public VisualPositionUpdateStategy getClone() {
        return new VisualPositionUpdateStategy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updatePosition(HoneyBee bee, HoneyBee otherBee) {
        int j = Rand.nextInt(bee.getDimension());

        Vector newPosition = bee.getPosition();
        Vector oldPosition = Vector.copyOf(bee.getPosition());
        Vector otherPosition = otherBee.getPosition();
        double value = newPosition.doubleValueOf(j);
        double other = otherPosition.doubleValueOf(j);
        newPosition.setReal(j, value + (Rand.nextDouble() * 2 - 1) * (value - other));

        //Determine if new position is better than old and update
        Fitness oldFitness = bee.getFitness().getClone();
        bee.calculateFitness();
        Fitness newFitness = bee.getFitness();
        if (newFitness.compareTo(oldFitness) < 0) {
            bee.setPosition(oldPosition);
            bee.getProperties().put(EntityType.FITNESS, oldFitness);
            return false;
        }

        return true;
    }
}
