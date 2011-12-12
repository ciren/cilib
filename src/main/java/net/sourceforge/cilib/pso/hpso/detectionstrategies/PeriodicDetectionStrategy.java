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

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import static com.google.common.base.Preconditions.checkArgument;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;

/**
 *
 * @author Filipe
 */
public class PeriodicDetectionStrategy implements BehaviorChangeTriggerDetectionStrategy {
    private ControlParameter period;

    public PeriodicDetectionStrategy() {
        period = new ConstantControlParameter(10.0);
    }

    /**
     * Construct a copy of the given {@link RandomDetectionStrategy}.
     *
     * @param copy the {@link RandomDetectionStrategy} to copy.
     */
    public PeriodicDetectionStrategy(PeriodicDetectionStrategy copy) {
        this.period = copy.period.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodicDetectionStrategy getClone() {
        return new PeriodicDetectionStrategy(this);
    }

    @Override
    public boolean detect(Entity entity) {
        checkArgument(entity instanceof Particle, "PeriodicDetectionStrategy can only be used with a Particle entity.");

        int iters = AbstractAlgorithm.get().getIterations();

        if (iters % period.getParameter() == 0) {
            return true;
        }

        return false;
    }

    public void setPeriod(double value) {
        period.setParameter(value);
    }
}
