/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.boa.positionupdatestrategies;

import net.sourceforge.cilib.boa.bee.HoneyBee;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Represents the visual exploration strategy used by bees to choose a their next
 * forage patch close by.
 * @author Andrich
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
        MersenneTwister twister = new MersenneTwister();
        int j = twister.nextInt(bee.getDimension());

        Vector newPosition = (Vector) bee.getPosition();
        Vector oldPosition = (Vector) bee.getPosition().getClone();
        Vector otherPosition = (Vector) otherBee.getPosition();
        double value = ((Real) newPosition.get(j)).getReal();
        double other = ((Real) otherPosition.get(j)).getReal();
        Real newValue = (Real) newPosition.get(j);
        newValue.set(value + (twister.nextDouble()*2-1)*(value - other));
        newPosition.set(j, newValue);

        //Determine if new position is better than old and update
        Fitness oldFitness =  bee.getFitness().getClone();
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
