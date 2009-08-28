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
package net.sourceforge.cilib.measurement.multiple;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.CoevolutionAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author leo
 * This class currently only works for PSO, it finds the best personal best fitness
 * values for each population in a multi-population based algorithm without re-calculating
 * the fitness.
 */
public class MultiPopulationFitness implements Measurement<Vector> {
    private static final long serialVersionUID = -608120128187899491L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Measurement getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return "T";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getValue(Algorithm algorithm) {
        Vector fitness = new Vector();
        CoevolutionAlgorithm ca = (CoevolutionAlgorithm) algorithm;
        for(PopulationBasedAlgorithm currentAlgorithm : ca) {
            Fitness best = null;
            for(Entity e: currentAlgorithm.getTopology().asList()){
                if(best == null || ((Fitness)e.getProperties().get(EntityType.Particle.BEST_FITNESS)).compareTo(best) > 0)
                    best = ((Fitness)e.getProperties().get(EntityType.Particle.BEST_FITNESS));
            }
            fitness.add(new Real(best.getValue()));
        }
        return fitness;
    }

}
