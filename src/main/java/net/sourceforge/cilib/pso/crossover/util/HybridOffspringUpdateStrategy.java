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
package net.sourceforge.cilib.pso.crossover.util;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *<p>
 * @INPROCEEDINGS{Løvbjerg01hybridparticle,
 *   author = {Morten Løvbjerg and Thomas Kiel Rasmussen and Thiemo Krink},
 *   title = {Hybrid Particle Swarm Optimiser with Breeding and Subpopulations},
 *   booktitle = {Proceedings of the Genetic and Evolutionary Computation Conference (GECCO-2001},
 *   year = {2001},
 *   pages = {469--476},
 *   publisher = {Morgan Kaufmann}
 * }
 * </p>
 */
public class HybridOffspringUpdateStrategy implements OffspringUpdateStrategy {
    @Override
    public List<Particle> updateOffspring(List<Particle> parents, List<Particle> offspring) {
        Particle p1 = parents.get(0);
        Particle p2 = parents.get(1);

        Vector v = ((Vector) p1.getVelocity()).plus((Vector) p2.getVelocity()).normalize();

        Particle o1 = offspring.get(0);
        Particle o2 = offspring.get(1);

        // TODO: add strategies to check if improves
        o1.getProperties().put(EntityType.Particle.VELOCITY, v.multiply(((Vector) p1.getVelocity()).length()));
        o2.getProperties().put(EntityType.Particle.VELOCITY, v.multiply(((Vector) p2.getVelocity()).length()));

        o1.getProperties().put(EntityType.Particle.BEST_POSITION, o1.getCandidateSolution());
        o2.getProperties().put(EntityType.Particle.BEST_POSITION, o2.getCandidateSolution());

        // TODO: this affects fitness evaluations
        o1.calculateFitness();
        o2.calculateFitness();

        o1.getProperties().put(EntityType.Particle.BEST_FITNESS, o1.getFitness());
        o2.getProperties().put(EntityType.Particle.BEST_FITNESS, o2.getFitness());
        
        return Arrays.asList(o1, o2);
    }    
}
