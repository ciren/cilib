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
package net.sourceforge.cilib.pso.pbestupdate;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Function;

public class MutatedDistinctPositionProvider extends DistinctPositionProvider {
    
    private ProbabilityDistributionFuction distribution;
    
    public MutatedDistinctPositionProvider() {
        this.distribution = new GaussianDistribution();
    }
    
    public MutatedDistinctPositionProvider(MutatedDistinctPositionProvider copy) {
        this.distribution = copy.distribution;
    }

    @Override
    public MutatedDistinctPositionProvider getClone() {
        return new MutatedDistinctPositionProvider(this);
    }

    @Override
    public Vector f(Particle particle) {
        return ((Vector) particle.getCandidateSolution()).map(new Function<Numeric, Numeric>() {
            public Numeric apply(Numeric input) {
                return Real.valueOf(input.doubleValue() + distribution.getRandomNumber(), input.getBounds());
            }                
        });
    }

    public void setDistribution(ProbabilityDistributionFuction distribution) {
        this.distribution = distribution;
    }

    public ProbabilityDistributionFuction getDistribution() {
        return distribution;
    }
}
