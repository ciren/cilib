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
package net.sourceforge.cilib.pso.crossover.pbestupdate;

import com.google.common.base.Supplier;
import java.util.List;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

public class NoisyPositionOffspringPBestProvider extends OffspringPBestProvider {    
    private ProbabilityDistributionFuction random;
    private OffspringPBestProvider delegate;

    public NoisyPositionOffspringPBestProvider() {
        this.random = new GaussianDistribution();
        this.delegate = new CurrentPositionOffspringPBestProvider();
    }

    @Override
    public StructuredType f(List<Particle> parents, Particle offspring) {
        return ((Vector) delegate.f(parents, offspring)).multiply(new Supplier<Number>(){
            @Override
            public Number get() {
                return random.getRandomNumber();
            }            
        });
    }

    public ProbabilityDistributionFuction getRandom() {
        return random;
    }

    public void setRandom(ProbabilityDistributionFuction random) {
        this.random = random;
    }

    public void setDelegate(OffspringPBestProvider delegate) {
        this.delegate = delegate;
    }

    public OffspringPBestProvider getDelegate() {
        return delegate;
    }
}
