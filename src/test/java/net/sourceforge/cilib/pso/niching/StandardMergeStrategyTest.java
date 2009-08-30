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
package net.sourceforge.cilib.pso.niching;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class StandardMergeStrategyTest {

    /**
     * The follow will test the absorption of two subswarms into a single
     * subswarm. To simplify the test, the particles will be located in a
     * two dimensional search space.
     *
     * Each sub-swarm will consist of the smallest valid number of particles
     * for a swarm, which is two. After the merge request (based on the defined
     * minimum in the strategy), only one swarm will remain.
     */
    @Test
    public void merge() {
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        pso1.setTopology(new GBestTopology<Particle>());
        pso2.setTopology(new GBestTopology<Particle>());

        Particle p1 = createParticle(new MinimisationFitness(0.0), new Real(0.0), new Real(0.0));
        Particle p2 = createParticle(new MinimisationFitness(1.0), new Real(Math.sqrt(0.6)), new Real(Math.sqrt(0.6)));
        Particle p3 = createParticle(new MinimisationFitness(2.0), new Real(Math.sqrt(0.3)), new Real(Math.sqrt(0.3)));
        Particle p4 = createParticle(new MinimisationFitness(3.0), new Real(1.0), new Real(1.0));

        pso1.getTopology().add(p1); pso1.getTopology().add(p2);
        pso2.getTopology().add(p3); pso2.getTopology().add(p4);

        Niche niche = new Niche();
        niche.addPopulationBasedAlgorithm(pso1);
        niche.addPopulationBasedAlgorithm(pso2);

        StandardMergeStrategy mergeStrategy = new StandardMergeStrategy();
        mergeStrategy.merge(niche);

        Assert.assertEquals(2, niche.getPopulations().size());
        Assert.assertEquals(2, niche.getPopulations().get(0).getTopology().size());
    }

    /**
     * Local helper method to create a StandardParticle with the provided Fitness and
     * with the proviced position.
     * @param fitness The fitness to assign the particle.
     * @param position The position in the search space to assign.
     * @return A constructed dummy particle.
     */
    private Particle createParticle(Fitness fitness, Real... position) {
        Particle particle = new StandardParticle();

        Vector vector = new Vector();
        for (Real real : position)
            vector.add(real);

        particle.setCandidateSolution(vector);
        particle.getProperties().put(EntityType.FITNESS, fitness);
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, vector);
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, fitness);

        return particle;
    }
}