/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import fj.F;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Implementation of the asynchronous iteration strategy for PSO.
 */
public class ASynchronousIterationStrategy extends AbstractIterationStrategy<PSO> {

    private static final long serialVersionUID = -3511991873784185698L;

    private F<Particle, Particle> additionalStep = new F<Particle, Particle>() {
        @Override
        public Particle f(Particle a) {
            return a;
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public ASynchronousIterationStrategy getClone() {
        return this;
    }

    /**
     * This is an ASynchronous strategy:
     *
     * <ol>
     *   <li>For all particles:</li>
     *   <ol>
     *     <li>Update the particle velocity</li>
     *     <li>Update the particle position</li>
     *     <li>Calculate the particle fitness</li>
     *     <li>For all particles in the current particle's neighbourhood</li>
     *     <ol>
     *       <li>Update the neighbourhood best</li>
     *     </ol>
     *   </ol>
     * </ol>
     *
     * @param algorithm the algorithm to which an iteration is to be applied.
     */
    public void performIteration(PSO algorithm) {
        Topology<Particle> topology = algorithm.getTopology();

        for (Particle current : topology) {
            current.updateVelocity();       // TODO: replace with visitor (will simplify particle interface)
            current.updatePosition();       // TODO: replace with visitor (will simplify particle interface)

            boundaryConstraint.enforce(current);
            current.calculateFitness();

            Particle newParticle = additionalStep.f(current);
            topology.set(topology.indexOf(current), newParticle);

            for (Particle other : topology.neighbourhood(current)) {
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(newParticle); // TODO: neighbourhood visitor?
                }
            }
        }
    }

    public void setAdditionalStep(F<Particle, Particle> additionalStep) {
        this.additionalStep = additionalStep;
    }

    public F<Particle, Particle> getAdditionalStep() {
        return additionalStep;
    }
}
