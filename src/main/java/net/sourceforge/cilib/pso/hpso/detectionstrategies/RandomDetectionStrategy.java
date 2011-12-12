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
package net.sourceforge.cilib.pso.hpso.detectionstrategies;

import static com.google.common.base.Preconditions.checkArgument;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 *
 * @author Filipe
 */
public class RandomDetectionStrategy implements BehaviorChangeTriggerDetectionStrategy {
    private ControlParameter probability;
    private RandomProvider random;

    public RandomDetectionStrategy() {
        probability = new ConstantControlParameter(75.0);
        random = new MersenneTwister();
    }

    /**
     * Construct a copy of the given {@link RandomDetectionStrategy}.
     *
     * @param copy the {@link RandomDetectionStrategy} to copy.
     */
    public RandomDetectionStrategy(RandomDetectionStrategy copy) {
        this.probability = copy.probability.getClone();
        this.random = copy.random;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomDetectionStrategy getClone() {
        return new RandomDetectionStrategy(this);
    }

    @Override
    public boolean detect(Entity entity) {
        checkArgument(entity instanceof Particle, "RandomDetectionStrategy can only be used with a Particle entity.");

        double rNum = random.nextDouble();

        if (rNum < probability.getParameter()) {
            return true;
        }

        return false;
    }

    public void setProbability(double value) {
        probability.setParameter(value);
    }
}
