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

import net.sourceforge.cilib.controlparameter.ParameterAdaptingPSOControlParameter;
import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 *
 */
public class StandardPersonalBestUpdateStrategyTest {

    /**
     * If a particle's current fitness is "more fit" than the current best
     * fitness, then the best fitness should equal the current fitness, the
     * best position should be updated to the current position, and the pbest
     * stagnation counter should <b>not be</b> incremented.
     */
    @Test
    public void testUpdatePersonalBest() {
        Particle particle = new StandardParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.0));
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));

        StandardPersonalBestUpdateStrategy strategy = new StandardPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(particle.getFitness()));
        Assert.assertThat(particle.getBestPosition(), is(particle.getPosition()));
        Assert.assertEquals(((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue(), 0);
    }

    /**
     * If a particle's current fitness is "less fit" than the current best
     * fitness, then the best fitness should <b>not be</b> equal the current fitness,
     * the best position should <b>not be</b> updated to the current position,
     * and the pbest stagnation counter should be incremented.
     */
    @Test
    public void testUpdatePersonalBestFails() {
        Particle particle = new StandardParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(100.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.0));
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));

        StandardPersonalBestUpdateStrategy strategy = new StandardPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(not(particle.getFitness())));
        Assert.assertThat(particle.getBestPosition(), is(not(particle.getPosition())));
        Assert.assertEquals(((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue(), 1);
    }
    
    
    @Test
    public void testUpdateParametizedPersonalBest() {
        ParameterizedParticle particle = new ParameterizedParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.0));
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
        ParameterAdaptingPSOControlParameter parameter = new BoundedModifiableControlParameter();
        ParameterAdaptingPSOControlParameter bestParameter = new BoundedModifiableControlParameter();
        bestParameter.setParameter(0.6);
        parameter.setParameter(0.55);
        particle.setInertia(parameter);
        particle.setSocialAcceleration(parameter);
        particle.setCognitiveAcceleration(parameter);
        particle.setVmax(parameter);

        StandardPersonalBestUpdateStrategy strategy = new StandardPersonalBestUpdateStrategy();
        strategy.updateParametizedPersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(particle.getFitness()));
        Assert.assertThat(particle.getBestPosition(), is(particle.getPosition()));
        Assert.assertEquals(((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue(), 0);
        Assert.assertThat(particle.getBestInertia().getParameter(), is(particle.getInertia().getParameter()));
        Assert.assertThat(particle.getBestSocialAcceleration().getParameter(), is(particle.getSocialAcceleration().getParameter()));
        Assert.assertThat(particle.getBestCognitiveAcceleration().getParameter(), is(particle.getCognitiveAcceleration().getParameter()));
        Assert.assertThat(particle.getBestVmax().getParameter(), is(particle.getVmax().getParameter()));
    }
}
