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
package net.sourceforge.cilib.util.selection.weighting;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;

/**
 *
 * @author filipe
 */
public class SpecializedRatio implements ParticleBehaviorRatio {
    private List<ParticleBehavior> behaviors;
    private List<Double> weights;

    @Override
    public double getRatio(ParticleBehavior particleBehavior) {
        checkState(behaviors.size() > 0, "You must add particle behaviors to the behavior pool first.");
        checkState(weights.size() == behaviors.size(), "Make sure the behavior pool is the same size as the weights list.");

        return weights.get(behaviors.indexOf(particleBehavior));
    }

    public void setBehaviors(List<ParticleBehavior> behaviors) {
        this.behaviors = behaviors;
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }
}