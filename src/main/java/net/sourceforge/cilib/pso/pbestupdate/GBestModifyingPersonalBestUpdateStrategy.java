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

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Update the personal best of the particle, based on the standard PSO definition
 * of the process.
 *
 * @author gpampara
 */
public class GBestModifyingPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {

    private static final long serialVersionUID = 266386833476786081L;
    private PersonalBestUpdateStrategy delegate;

    public GBestModifyingPersonalBestUpdateStrategy() {
        this.delegate = new StandardPersonalBestUpdateStrategy();
    }

    public GBestModifyingPersonalBestUpdateStrategy(GBestModifyingPersonalBestUpdateStrategy copy) {
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GBestModifyingPersonalBestUpdateStrategy getClone() {
        return new GBestModifyingPersonalBestUpdateStrategy(this);
    }

    /**
     * If the current fitness is better than the current best fitness, update
     * the best fitness of the particle to equal the current fitness and make
     * the personal best position a clone of the current particle position.
     *
     * If the current fitness is not better than the current best fitness,
     * increase the particle's pbest stagnation counter.
     *
     * @param particle The particle to update.
     */
    @Override
    public void updatePersonalBest(Particle particle) {
        delegate.updatePersonalBest(particle);

        Particle gBest = particle.getNeighbourhoodBest();

        for(int j = 0; j < particle.getPosition().size(); j++) {
            //if(random.nextDouble() < learningProbability.get(particle)) {
                Particle gBestClone = gBest.getClone();
                Vector gBestVector = (Vector) gBestClone.getBestPosition();

                gBestVector.setReal(j, ((Vector)particle.getPosition()).doubleValueOf(j));
                gBestClone.setCandidateSolution(gBestVector);
                Fitness fitness = particle.getFitnessCalculator().getFitness(gBestClone);

                if(fitness.compareTo(gBest.getBestFitness()) > 0) {
                    gBest.getProperties().put(EntityType.Particle.BEST_POSITION, gBestVector);
                    gBest.getProperties().put(EntityType.Particle.BEST_FITNESS, fitness);
                }
            //}
        }
    }
}
