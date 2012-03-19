/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @param <E> The entity type.
 */
public class RandomBoundedInitializationStrategy<E extends Entity> implements
        InitializationStrategy<E> {

    private static final long serialVersionUID = -7926839076670354209L;
    private ControlParameter lowerBound;
    private ControlParameter upperBound;
    private ProbabilityDistributionFuction random;

    public RandomBoundedInitializationStrategy() {

        this.lowerBound = ConstantControlParameter.of(0.1);
        this.upperBound = ConstantControlParameter.of(0.1);
 this.random = new UniformDistribution();

    }

    public RandomBoundedInitializationStrategy(RandomBoundedInitializationStrategy copy) {
        this.lowerBound = copy.lowerBound;
        this.upperBound = copy.upperBound;
        this.random = copy.random;
    }

    @Override
    public RandomBoundedInitializationStrategy getClone() {
        return new RandomBoundedInitializationStrategy(this);
    }

    @Override
    public void initialize(Enum<?> key, E entity) {
        Type type = entity.getProperties().get(key);
        Vector velocity = (Vector) type;

        for (int i = 0; i < velocity.size(); i++) {
            velocity.setReal(i, random.getRandomNumber(lowerBound.getParameter(), upperBound.getParameter()));
        }
    }

    public ControlParameter getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(ControlParameter lowerBound) {
        this.lowerBound = lowerBound;
    }

    public ControlParameter getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(ControlParameter upperBound) {
        this.upperBound = upperBound;
    }
}
