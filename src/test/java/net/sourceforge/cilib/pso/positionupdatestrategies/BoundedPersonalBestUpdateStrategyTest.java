/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 *
 * @author gpampara
 */
public class BoundedPersonalBestUpdateStrategyTest {

    @Test
    public void updatePersonalBest() {
        Real real = new Real(-5.0, 5.0);
        real.setReal(0.0);

        Particle particle = new StandardParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, new Vector(1, real));

        BoundedPersonalBestUpdateStrategy strategy = new BoundedPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(particle.getFitness()));
        Assert.assertThat(particle.getBestPosition(), is(particle.getPosition()));
    }

    @Test
    public void updatePersonalBestFails() {
        Real real = new Real(-5.0, 5.0);
        real.setReal(-10.0);

        Particle particle = new StandardParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, new Vector(1, real));

        Type previousBestPosition = particle.getBestPosition();

        BoundedPersonalBestUpdateStrategy strategy = new BoundedPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(not(particle.getFitness())));
        Assert.assertThat(particle.getBestPosition(), is(previousBestPosition));
        Assert.assertThat(particle.getFitness(), is(InferiorFitness.instance()));
    }

}