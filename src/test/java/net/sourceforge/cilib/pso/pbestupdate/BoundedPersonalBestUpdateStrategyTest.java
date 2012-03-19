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

import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 *
 */
public class BoundedPersonalBestUpdateStrategyTest {

    @Test
    public void testUpdatePersonalBest() {
        Real real = Real.valueOf(0.0, new Bounds(-5.0, 5.0));
        Particle particle = new StandardParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(real));

        BoundedPersonalBestUpdateStrategy strategy = new BoundedPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(particle.getFitness()));
        Assert.assertThat(particle.getBestPosition(), is(particle.getPosition()));
        
    }

    @Test
    public void testUpdatePersonalBestFails() {
        Real real = Real.valueOf(-10.0, new Bounds(-5.0, 5.0));
        Particle particle = new StandardParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(real));

        Type previousBestPosition = particle.getBestPosition();

        BoundedPersonalBestUpdateStrategy strategy = new BoundedPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(not(particle.getFitness())));
        Assert.assertThat(particle.getBestPosition(), is(previousBestPosition));
        Assert.assertThat(particle.getFitness(), is(InferiorFitness.instance()));
    }

    /**
     * Test of updateParametizedPersonalBest method, of class BoundedPersonalBestUpdateStrategy.
     */
    @Test
    public void testUpdateParametizedPersonalBest() {
        System.out.println("updateParametizedPersonalBest");
        Real real = Real.valueOf(0.0, new Bounds(-5.0, 5.0));
        ParameterizedParticle particle = new ParameterizedParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(real));
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setLowerBound(0.0);
        parameter.setUpperBound(0.9);
        parameter.setParameter(0.55);
        particle.setInertia(parameter);
        particle.setSocialAcceleration(parameter);
        particle.setCognitiveAcceleration(parameter);
        particle.setVmax(parameter);
        
        BoundedPersonalBestUpdateStrategy strategy = new BoundedPersonalBestUpdateStrategy();
        strategy.updateParametizedPersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(particle.getFitness()));
        Assert.assertThat(particle.getBestPosition(), is(particle.getPosition()));
        Assert.assertThat(particle.getBestInertia().getParameter(), is(particle.getInertia().getParameter()));
        Assert.assertThat(particle.getBestSocialAcceleration().getParameter(), is(particle.getSocialAcceleration().getParameter()));
        Assert.assertThat(particle.getBestCognitiveAcceleration().getParameter(), is(particle.getCognitiveAcceleration().getParameter()));
        Assert.assertThat(particle.getBestVmax().getParameter(), is(particle.getVmax().getParameter()));
    }
    
    /**
     * Test that updateParametizedPersonalBest method fails, of class BoundedPersonalBestUpdateStrategy.
     */
    @Test
    public void testUpdateParametizedPersonalBestFails() {
        System.out.println("updateParametizedPersonalBest Fails");
        Real real = Real.valueOf(0.0, new Bounds(-5.0, 5.0));
        ParameterizedParticle particle = new ParameterizedParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(real));
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setUpperBound(0.5);
        parameter.setLowerBound(0.0);
        parameter.setParameter(-1.0);
        particle.setInertia(parameter);
        particle.setSocialAcceleration(parameter);
        particle.setCognitiveAcceleration(parameter);
        particle.setVmax(parameter);
        
        BoundedPersonalBestUpdateStrategy strategy = new BoundedPersonalBestUpdateStrategy();
        strategy.updateParametizedPersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(not(particle.getFitness())));
        Assert.assertThat(particle.getBestPosition(), is(not(particle.getPosition())));
        Assert.assertThat(particle.getBestInertia().getParameter(), is(not(particle.getInertia().getParameter())));
        Assert.assertThat(particle.getBestSocialAcceleration().getParameter(), is(not(particle.getSocialAcceleration().getParameter())));
        Assert.assertThat(particle.getBestCognitiveAcceleration().getParameter(), is(not(particle.getCognitiveAcceleration().getParameter())));
        Assert.assertThat(particle.getBestVmax().getParameter(), is(not(particle.getVmax().getParameter())));
    }
}
