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
package net.sourceforge.cilib.pso.positionprovider;

import java.util.ArrayList;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/** Implementation of the DE PSO of Hendtlass.
 * TODO: can the DE strategies be incorporated somehow?
 * @author Andries Engelbrecht
 */
public class DEPositionProvider implements PositionProvider {

    private static final long serialVersionUID = -4052606351661988520L;
    private ProbabilityDistributionFuction differentialEvolutionProbability; //Make a parameter to set via xml
    private ProbabilityDistributionFuction crossoverProbability;
    private ProbabilityDistributionFuction scaleParameter;
    private ProbabilityDistributionFuction rand1;
    private ProbabilityDistributionFuction rand2;
    private ProbabilityDistributionFuction rand3;
    private ProbabilityDistributionFuction rand4;

    public DEPositionProvider() {
        differentialEvolutionProbability = new GaussianDistribution();
        rand1 = new UniformDistribution();
        rand2 = new UniformDistribution();
        rand3 = new UniformDistribution();
        rand4 = new UniformDistribution();
        crossoverProbability = new GaussianDistribution();
        scaleParameter = new GaussianDistribution();
    }

    public DEPositionProvider(DEPositionProvider copy) {
        this();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DEPositionProvider getClone() {
        return new DEPositionProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector position = (Vector) particle.getPosition();
        Vector velocity = (Vector) particle.getVelocity();

        if (rand1.getRandomNumber() < differentialEvolutionProbability.getRandomNumber(0.8, 0.1)) {
            return Vectors.sumOf(position, velocity);
        } else {
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
                int random = rand2.getRandomProvider().nextInt(pso.getTopology().size());
                Entity parent = pso.getTopology().get(random);
                if (!positions.contains((Vector) parent.getCandidateSolution())) {
                    positions.add((Vector) parent.getCandidateSolution());
                    count++;
                }
            }

            Vector position1 = positions.get(0);
            Vector position2 = positions.get(1);
            Vector position3 = positions.get(2);

            Vector dePosition = Vector.copyOf(position);
            int j = Double.valueOf(rand3.getRandomNumber(0, position.size())).intValue();
            for (int i = 0; i < position.size(); ++i) {
                if ((rand4.getRandomNumber(0, 1) < crossoverProbability.getRandomNumber(0.5, 0.3)) || (j == i)) {
                    double value = position1.doubleValueOf(i);
                    value += scaleParameter.getRandomNumber(0.7, 0.3) * (position2.doubleValueOf(i) - position3.doubleValueOf(i));
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
            return dePosition;
        }
    }
}
