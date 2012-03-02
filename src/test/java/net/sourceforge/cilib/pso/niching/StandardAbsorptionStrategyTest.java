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
package net.sourceforge.cilib.pso.niching;

import java.util.Arrays;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.niching.absorption.AbsorptionStrategy;
import net.sourceforge.cilib.pso.niching.absorption.StandardAbsorptionStrategy;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 */
public class StandardAbsorptionStrategyTest {

    /**
     * Absorb an entity from the main swarm then the entity drifts
     * into the sub-swarm.
     */
    @Test
    public void absorption() {
        Particle p1 = createParticle(new MinimisationFitness(0.0), Real.valueOf(0.0));
        Particle p2 = createParticle(new MinimisationFitness(1.0), Real.valueOf(1.0));
        Particle p3 = createParticle(new MinimisationFitness(0.5), Real.valueOf(0.5));
        Particle p4 = createParticle(new MinimisationFitness(5.0), Real.valueOf(5.0));

        PSO mainSwarm = new PSO();
        mainSwarm.getTopology().addAll(Arrays.asList(p3, p4));

        PSO subswarm = new PSO();
        subswarm.getTopology().addAll(Arrays.asList(p1, p2));

        Niche niche = new Niche();
        niche.setMainSwarm(mainSwarm);
        niche.addPopulationBasedAlgorithm(subswarm);

        AbsorptionStrategy absorptionStrategy = new StandardAbsorptionStrategy();
        absorptionStrategy.absorb(niche);

        Assert.assertEquals(1, niche.getMainSwarm().getTopology().size());
        Assert.assertEquals(3, subswarm.getTopology().size());
        Assert.assertThat((Particle) niche.getMainSwarm().getTopology().get(0), is(p4));
    }

    private Particle createParticle(Fitness fitness, Real position) {
        Particle particle = new StandardParticle();

        Vector vector = new Vector();
        vector.add(position);

        particle.setCandidateSolution(vector);
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, vector);

        particle.getProperties().put(EntityType.FITNESS, fitness);
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, fitness);

        return particle;
    }

}