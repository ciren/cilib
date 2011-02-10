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
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.Int;

/**
 * This {@link BehaviorChangeTriggerDetectionStrategy} monitors a
 * {@link Particle}'s personal best fitness. If it has not change over a
 * recent window of iterations, the particle is assumed to have stagnated
 * and should therefore change its behavior.
 *
 * @author Bennie Leonard
 */
public class PersonalBestStagnationDetectionStrategy implements BehaviorChangeTriggerDetectionStrategy {
    private ControlParameter windowSize;

    /**
     * Construct a new {@link PersonalBestStagnationDetectionStrategy} with a
     * default window size of 10.
     */
    public PersonalBestStagnationDetectionStrategy() {
        windowSize = new ConstantControlParameter(10);
    }

    /**
     * Construct a copy of the given {@link PersonalBestStagnationDetectionStrategy}.
     *
     * @param copy the {@link PersonalBestStagnationDetectionStrategy} to copy.
     */
    public PersonalBestStagnationDetectionStrategy(PersonalBestStagnationDetectionStrategy copy) {
        this.windowSize = copy.windowSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonalBestStagnationDetectionStrategy getClone() {
        return new PersonalBestStagnationDetectionStrategy(this);
    }

    /**
     * Checks that a {@link Particle}'s personal best fitness has changed
     * during a recent window of iterations (number of iterations set by
     * <code>windowSize</code>). If it has not, the particle is assumed to
     * be stagnating.
     *
     * @param entity The {@link Particle} to check for stagnation.
     * @return True if the {@link Particle} is stagnating. False otherwise.
     */
    @Override
    public boolean detect(Entity entity) {
        checkArgument(entity instanceof Particle, "PersonalBestStagnationDetectionStrategy can only be used with a Particle entity.");

        int counter = ((Int)entity.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue();

        if (counter > windowSize.getParameter()) {
            return true;
        }

        return false;
    }

    public void setWindowSize(double value) {
        windowSize.setParameter(value);
    }
}
