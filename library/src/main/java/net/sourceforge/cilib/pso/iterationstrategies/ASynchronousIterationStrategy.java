/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import fj.F;
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;

/**
 * Implementation of the asynchrounous iteration strategy for PSO.
 *
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
     * <p>This is an ASynchronous strategy:</p>
     * <ol>
     * <li>For all particles:</li>
     * <ol>
     * <li>Update the particle velocity</li>
     * <li>Update the particle position</li>
     * <li>Calculate the particle fitness</li>
     * <li>For all paritcles in the current particle's neighbourhood</li>
     * <ol><li>Update the nieghbourhooh best</li></ol>
     * </ol>
     * </ol>
     *
     * @see net.sourceforge.cilib.PSO.IterationStrategy#performIteration()
     * @param algorithm The algorithm to which an iteration is to be applied.
     */
    public void performIteration(PSO algorithm) {
        Topology<Particle> topology = algorithm.getTopology();

        for (Iterator<? extends Particle> i = topology.iterator(); i.hasNext();) {
            Particle current = i.next();
            current.updateVelocity();       // TODO: replace with visitor (will simplify particle interface)
            current.updatePosition();       // TODO: replace with visitor (will simplify particle interface)

            boundaryConstraint.enforce(current);

            current.calculateFitness();

            Particle newParticle = additionalStep.f(current);
            topology.set(topology.indexOf(current), newParticle);

            for (Iterator<? extends Particle> j = topology.neighbourhood(i); j.hasNext();) {
                Particle other = j.next();
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
