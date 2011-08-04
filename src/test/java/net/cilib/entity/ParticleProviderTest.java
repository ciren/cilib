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
package net.cilib.entity;

import com.google.common.collect.Iterables;
import fj.data.List;
import net.cilib.collection.Topology;
import fj.data.Option;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.problem.Evaluatable;
import net.cilib.pso.PositionProvider;
import net.cilib.pso.VelocityProvider;
import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static net.cilib.predef.Predef.*;

/**
 *
 */
public class ParticleProviderTest {
    private static final Particle OLD_PARTICLE = new Particle(solution(1.0),
            solution(1.0), velocity(0.0), Option.some(1.0));

    @Test
    public void newParticleCreation() {
        final Evaluatable problem = mock(Evaluatable.class);
        final PositionProvider position = mock(PositionProvider.class);
        final VelocityProvider velocity = mock(VelocityProvider.class);

        when(position.f(any(List.class), any(List.class))).thenReturn(solution(2.0));
        when(velocity.f(any(Particle.class), any(Topology.class))).thenReturn(velocity(1.0));
        when(problem.evaluate(any(List.class))).thenReturn(Option.<Double>none());

        final FitnessProvider fitness = new FitnessProvider(problem);
        final ParticleProvider provider = new ParticleProvider(position, velocity, fitness, FitnessComparator.MAX);
        final Particle p = provider.basedOn(OLD_PARTICLE).get(ImmutableGBestTopology.of());

        Assert.assertNotSame(p, OLD_PARTICLE);
        verify(velocity, times(1)).f(any(Particle.class), any(Topology.class));
        verify(position, times(1)).f(any(List.class), any(List.class));
    }

    @Test
    public void newParticleBestPositionUpdated() {
        final PositionProvider p = mock(PositionProvider.class);
        final VelocityProvider v = mock(VelocityProvider.class);
        final FitnessProvider f = mock(FitnessProvider.class);

        when(p.f(any(List.class), any(List.class))).thenReturn(solution(2.0));
        when(v.f(any(Particle.class), any(Topology.class))).thenReturn(velocity(1.0));
        when(f.evaluate(any(List.class))).thenReturn(Option.some(1.5));

        final ParticleProvider provider = new ParticleProvider(p, v, f, FitnessComparator.MAX);
        final Particle newPart = provider.basedOn(OLD_PARTICLE).get(ImmutableGBestTopology.of());

        Assert.assertThat(newPart.memory(), not(equalTo(solution(1.0))));
    }

    @Test
    public void oldBestPositionMaintainedInNewParticle() {
        final PositionProvider p = mock(PositionProvider.class);
        final VelocityProvider v = mock(VelocityProvider.class);
        final FitnessProvider f = mock(FitnessProvider.class);

        when(p.f(any(List.class), any(List.class))).thenReturn(solution(2.0));
        when(v.f(any(Particle.class), any(Topology.class))).thenReturn(velocity(1.0));
        when(f.evaluate(any(List.class))).thenReturn(Option.some(0.5));

        final ParticleProvider provider = new ParticleProvider(p, v, f, FitnessComparator.MAX);
        final Particle newPart = provider.basedOn(OLD_PARTICLE).get(ImmutableGBestTopology.of());

        Assert.assertTrue(Iterables.elementsEqual(newPart.memory(), solution(1.0)));
    }

    @Test
    public void newVelocityCreated() {
        final PositionProvider p = mock(PositionProvider.class);
        final VelocityProvider v = mock(VelocityProvider.class);
        final Evaluatable problem = mock(Evaluatable.class);

        when(v.f(any(Particle.class), any(Topology.class))).thenReturn(velocity(0.0));
        when(p.f(any(List.class), any(List.class))).thenReturn(solution(1.0));
        when(problem.evaluate(any(List.class))).thenReturn(Option.<Double>none());

        final ParticleProvider provider = new ParticleProvider(p, v, new FitnessProvider(problem), FitnessComparator.MAX);
        provider.basedOn(OLD_PARTICLE).get(ImmutableGBestTopology.of());

        verify(v, times(1)).f(any(Particle.class), any(Topology.class));
    }

    @Test
    public void newPositionCreated() {
        final PositionProvider p = mock(PositionProvider.class);
        final VelocityProvider v = mock(VelocityProvider.class);
        final Evaluatable problem = mock(Evaluatable.class);

        when(v.f(any(Particle.class), any(Topology.class))).thenReturn(velocity(0.0));
        when(p.f(any(List.class), any(List.class))).thenReturn(solution(1.0));
        when(problem.evaluate(any(List.class))).thenReturn(Option.<Double>none());

        final ParticleProvider provider = new ParticleProvider(p, v, new FitnessProvider(problem), FitnessComparator.MAX);
        provider.basedOn(OLD_PARTICLE).get(ImmutableGBestTopology.of());

        verify(p, times(1)).f(any(List.class), any(List.class));
    }
}
