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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.LinearDecreasingControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of the FDR-PSO velocity update equation.
 *
 * <p>
 * BibTex entry:<br/>
 * <code>
 * &#64;ARTICLE{1202264,<br>
 * title={Fitness-distance-ratio based particle swarm optimization},<br>
 * author={Peram, T. and Veeramachaneni, K. and Mohan, C.K.},<br>
 * journal={Swarm Intelligence Symposium, 2003. SIS '03. Proceedings of the 2003 IEEE},<br>
 * year={2003},<br>
 * month={April},<br>
 * volume={},<br>
 * number={},<br>
 * pages={ 174-181},<br>
 * abstract={ This paper presents a modification of the particle swarm optimization algorithm (PSO) intended to combat the problem of premature convergence observed in many applications of PSO. The proposed new algorithm moves particles towards nearby particles of higher fitness, instead of attracting each particle towards just the best position discovered so far by any particle. This is accomplished by using the ratio of the relative fitness and the distance of other particles to determine the direction in which each component of the particle position needs to be changed. The resulting algorithm (FDR-PSO) is shown to perform significantly better than the original PSO algorithm and some of its variants, on many different benchmark optimization problems. Empirical examination of the evolution of the particles demonstrates that the convergence of the algorithm does not occur at an early phase of particle evolution, unlike PSO. Avoiding premature convergence allows FDR-PSO to continue search for global optima in difficult multimodal optimization problems.},<br>
 * keywords={ convergence of numerical methods, evolutionary computation, optimisation, search problems FDR-PSO, fitness-distance ratio, global optima search, multimodal optimization problems, particle position, particle swarm optimization, premature convergence, relative fitness},<br>
 * doi={10.1109/SIS.2003.1202264},<br>
 * ISSN={ }, }<br>
 * </code>
 * </p>
 *
 * @author Olusegun Olorunda
 */
public class FDRVelocityUpdateStrategy extends StandardVelocityUpdate {
    private static final long serialVersionUID = -7117135203986406944L;
    protected ControlParameter fdrMaximizerAcceleration;

    public FDRVelocityUpdateStrategy() {
        inertiaWeight = new LinearDecreasingControlParameter();
        fdrMaximizerAcceleration = new ConstantControlParameter();

        cognitiveAcceleration.setParameter(1);
        socialAcceleration.setParameter(1);
        fdrMaximizerAcceleration.setParameter(2);
    }

    public FDRVelocityUpdateStrategy(FDRVelocityUpdateStrategy copy) {
        super(copy);
        this.fdrMaximizerAcceleration = copy.fdrMaximizerAcceleration.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FDRVelocityUpdateStrategy getClone() {
        return new FDRVelocityUpdateStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateVelocity(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector bestPosition = (Vector) particle.getBestPosition();
        Vector neighbourhoodBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();

        for (int i = 0; i < particle.getDimension(); ++i) {
            Topology<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
            Iterator<Particle> swarmIterator = topology.iterator();
            Particle fdrMaximizer = swarmIterator.next();
            double maxFDR = 0.0;

            while (swarmIterator.hasNext()) {
                Particle currentTarget = swarmIterator.next();

                if (currentTarget.getId() != particle.getId()) {
                    Fitness currentTargetFitness = currentTarget.getBestFitness();
                    Vector currentTargetPosition = (Vector) currentTarget.getBestPosition();

                    double fitnessDifference = (currentTargetFitness.getValue() - particle.getFitness().getValue());
                    double testFDR = fitnessDifference / Math.abs(position.doubleValueOf(i) - currentTargetPosition.doubleValueOf(i));

                    if (testFDR > maxFDR) {
                        maxFDR = testFDR;
                        fdrMaximizer = currentTarget;
                    }
                }
            }

            Vector fdrMaximizerPosition = (Vector) fdrMaximizer.getBestPosition();

            double value = (inertiaWeight.getParameter() * velocity.doubleValueOf(i)) +
                        cognitiveAcceleration.getParameter() * (bestPosition.doubleValueOf(i) - position.doubleValueOf(i)) +
                        socialAcceleration.getParameter() * (neighbourhoodBestPosition.doubleValueOf(i) - position.doubleValueOf(i)) +
                        fdrMaximizerAcceleration.getParameter() * r1.nextDouble() * (fdrMaximizerPosition.doubleValueOf(i) - position.doubleValueOf(i));

            velocity.setReal(i, value);
            clamp(velocity, i);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateControlParameters(Particle particle) {
        super.updateControlParameters(particle);
        fdrMaximizerAcceleration.updateParameter();
    }

    /**
     * @return the fdrMaximizerAcceleration
     */
    public ControlParameter getFdrMaximizerAcceleration() {
        return fdrMaximizerAcceleration;
    }

    /**
     * @param fdrMaximizerAcceleration
     *            the fdrMaximizerAcceleration to set
     */
    public void setFdrMaximizerAcceleration(ControlParameter fdrMaximizerAcceleration) {
        this.fdrMaximizerAcceleration = fdrMaximizerAcceleration;
    }

}
