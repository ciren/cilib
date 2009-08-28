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

import java.util.ArrayList;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;

/** Implementation of the DE PSO of Hendtlass.
 * TODO: can the DE strategies be incorporated somehow?
 * @author Andries Engelbrecht
 */
public class DEPositionUpdateStrategy implements PositionUpdateStrategy {
    private static final long serialVersionUID = -4052606351661988520L;

    private RandomNumber differentialEvolutionProbability; //Make a parameter to set via xml
    private RandomNumber crossoverProbability;
    private RandomNumber scaleParameter;
    private RandomNumber rand1;
    private RandomNumber rand2;
    private RandomNumber rand3;
    private RandomNumber rand4;

    public DEPositionUpdateStrategy() {
       differentialEvolutionProbability = new RandomNumber();
       rand1 = new RandomNumber();
       rand2 = new RandomNumber();
       rand3 = new RandomNumber();
       rand4 = new RandomNumber();
       crossoverProbability = new RandomNumber();
       scaleParameter = new RandomNumber();
    }

    public DEPositionUpdateStrategy(DEPositionUpdateStrategy copy) {
        this();
    }

    /**
     * {@inheritDoc}
     */
    public DEPositionUpdateStrategy getClone() {
        return new DEPositionUpdateStrategy(this);
    }

    public void updatePosition(Particle particle) {
        Vector position = (Vector) particle.getPosition();
        Vector velocity = (Vector) particle.getVelocity();

        if (rand1.getUniform() < differentialEvolutionProbability.getGaussian(0.8, 0.1)) {
            particle.setCandidateSolution(position.plus(velocity));
        }
        else {
            ArrayList<Vector> positions = new ArrayList<Vector>(3);

            //select three random individuals, all different and different from particle
            PSO pso = (PSO) AbstractAlgorithm.get();

            /*Iterator k = pso.getTopology().iterator();
            int counter = 0;
            String particleId = particle.getId();
            Vector pos;
            while (k.hasNext() && (counter < 3)) {
                Particle p = (Particle) k.next();
                if ((p.getId() != particleId) && (rand2.getUniform(0,1) <= 0.5)) {
                    pos = (Vector) p.getPosition();
                    positions.add(pos);
                    counter++;
                }
            }*/

            int count = 0;

            while (count < 3) {
                int random = rand2.getRandomGenerator().nextInt(pso.getTopology().size());
                Entity parent = pso.getTopology().get(random);
                if (!positions.contains(parent)) {
                    positions.add((Vector) parent.getCandidateSolution());
                    count++;
                }
            }

            Vector position1 = positions.get(0);
            Vector position2 = positions.get(1);
            Vector position3 = positions.get(2);

            Vector dePosition = position.getClone();
            int j = Double.valueOf(rand3.getUniform(0, position.getDimension())).intValue();
            for (int i = 0; i < position.getDimension(); ++i) {
                if ((rand4.getUniform(0, 1) < crossoverProbability.getGaussian(0.5, 0.3)) || (j == i)) {
                    double value = position1.getReal(i);
                    value += scaleParameter.getGaussian(0.7, 0.3) * (position2.getReal(i) - position3.getReal(i));

                    dePosition.setReal(i, value);
                }
                    //else
                        //DEposition.setReal(i, )add(new Real(position3.getReal(i)));
            }


            //position should only become the offspring if its fitness is better
            Fitness trialFitness = pso.getOptimisationProblem().getFitness(dePosition);
            Fitness currentFitness = pso.getOptimisationProblem().getFitness(particle.getCandidateSolution());

            if (trialFitness.compareTo(currentFitness) > 0) {
                particle.setCandidateSolution(dePosition);
            }
        }
    }
}
