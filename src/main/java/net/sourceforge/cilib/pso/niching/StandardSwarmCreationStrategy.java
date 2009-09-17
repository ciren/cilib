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

import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.ClosestEntityVisitor;
import net.sourceforge.cilib.problem.boundaryconstraint.ReinitialisationBoundary;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;

/**
 * <p>
 * Create a set of niching locations, based on a provided set of identified
 * niching entities.
 * </p>
 * <p>
 * For each newly discovered niching location, a new sub-swarm is creates that will
 * maintain the niche. For the case of the PSO, the niching particle and the closest
 * particle to the identified particle are gropuped into a niche. Sub-swarms will always
 * then have at least two particles.
 * </p>
 * <p>
 * The rational for two particles is that a particle is a social entity and as a result
 * needs to share information. Ensuring that there are at least two particles within
 * a sub-swarm will enable the velocity update equation associated with the particle
 * to still operate.
 * </p>
 * @author gpampara
 */
public class StandardSwarmCreationStrategy implements NicheCreationStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Niche algorithm, List<Entity> niches) {
        Topology<? extends Entity> mainSwarm = algorithm.getMainSwarm().getTopology();

        if (mainSwarm.size() < 2)
            return;

        for (int i = 0; i < niches.size(); i++) {
            Entity niche = niches.get(i);

            // Determine the closest Entity to the current
            ClosestEntityVisitor closestEntityVisitor = new ClosestEntityVisitor();
            closestEntityVisitor.setTargetEntity(niche);
            mainSwarm.accept(closestEntityVisitor);

            Particle nicheMainParticle = (Particle) niche;
            Particle nicheClosestParticle = (Particle) closestEntityVisitor.getResult();

            mainSwarm.remove(nicheMainParticle);
            mainSwarm.remove(nicheClosestParticle);

            niches.remove(nicheMainParticle);
            niches.remove(nicheClosestParticle);

            nicheMainParticle.setNeighbourhoodBest(nicheMainParticle);
            nicheClosestParticle.setNeighbourhoodBest(nicheMainParticle);

            PSO pso = new PSO();
            ((SynchronousIterationStrategy)pso.getIterationStrategy()).setBoundaryConstraint(new ReinitialisationBoundary());
            pso.setOptimisationProblem(algorithm.getOptimisationProblem());
            pso.getTopology().add(nicheMainParticle);
            pso.getTopology().add(nicheClosestParticle);

            // Add the newly created niche to the list of maintained sub populations.
            algorithm.addPopulationBasedAlgorithm(pso);
        }
    }

}
