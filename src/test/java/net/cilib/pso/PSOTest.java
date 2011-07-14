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
package net.cilib.pso;

import com.google.common.collect.Iterables;
import fj.data.List;
import fj.data.Option;
import junit.framework.Assert;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.FitnessComparator;
import net.cilib.entity.FitnessProvider;
import net.cilib.entity.Particle;
import net.cilib.entity.ParticleProvider;
import net.cilib.problem.Evaluatable;
import static net.cilib.predef.Predef.*;
import static org.mockito.Mockito.*;
import org.junit.Test;

/**
 * @author gpampara
 */
public class PSOTest {

    @Test
    public void algorithmCreation() {
        final PositionProvider p = mock(PositionProvider.class);
        final VelocityProvider v = mock(VelocityProvider.class);
        final Evaluatable problem = mock(Evaluatable.class);

        when(v.f(any(Particle.class), any(Topology.class))).thenReturn(List.<Double>list(0.0));
        when(p.f(any(List.class), any(List.class))).thenReturn(solution(1.0));
        when(problem.evaluate(any(List.class))).thenReturn(Option.<Double>some(1.0));

        ParticleProvider provider = new ParticleProvider(p, v, new FitnessProvider(problem), FitnessComparator.MAX);
        PSO pso = new PSO(provider);

        Topology<Particle> next = pso.next(ImmutableGBestTopology.topologyOf(newParticle(solution(1.0))));

        Assert.assertTrue(Iterables.size(next) == 1);
    }

    @Test
    public void iterationCreatesNewParticlesFromOld() {
        PositionProvider p = mock(PositionProvider.class);
        VelocityProvider v = mock(VelocityProvider.class);
        FitnessProvider f = mock(FitnessProvider.class);
        ParticleProvider provider = new ParticleProvider(p, v, f, FitnessComparator.MAX);

        when(p.f(any(List.class), any(List.class))).thenReturn(solution(1.0));
        when(v.f(any(Particle.class), any(Topology.class))).thenReturn(velocity(0.0));
        when(f.evaluate(any(List.class))).thenReturn(Option.some(1.0));

        PSO pso = new PSO(provider);

        Particle particle = newParticle(solution(1.0));
        Topology<Particle> next = pso.next(ImmutableGBestTopology.topologyOf(particle));

        Assert.assertTrue(Iterables.size(next) == 1);
        Assert.assertNotSame(next.iterator().next(), particle);
    }

    private Particle newParticle(List<Double> solution) {
        return new Particle(solution, solution,
                List.<Double>replicate(solution.length(), 0.0),
                Option.some(1.0));
    }
}
