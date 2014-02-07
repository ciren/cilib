/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import fj.F;
import fj.F2;
import fj.Function;
import fj.P2;
import fj.data.List;

/**
 * Implementation of the asynchronous iteration strategy for PSO.
 */
public class ASynchronousIterationStrategy extends AbstractIterationStrategy<PSO> {

    private static final long serialVersionUID = -3511991873784185698L;

    private F<Particle, Particle> additionalStep = Function.identity();

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
    @Override
    public void performIteration(final PSO algorithm) {
        final fj.data.List<Particle> topology = algorithm.getTopology();

        algorithm.setTopology(topology.zipIndex().foldLeft(new F2<fj.data.List<Particle>, P2<Particle, Integer>, fj.data.List<Particle>>() {
			@Override
			public List<Particle> f(List<Particle> accum, P2<Particle, Integer> item) {
				item._1().getBehaviour().performIteration(item._1());
                
	            Particle newParticle = additionalStep.f(item._1());
                List<Particle> result = accum.snoc(newParticle);
	            fj.data.List<Particle> intermediate = result.append(topology.drop(item._2()+1));

	            for (Particle other : algorithm.getNeighbourhood().f(intermediate, newParticle)) {
	                if (newParticle.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
	                    other.setNeighbourhoodBest(newParticle);
	                }
	            }
	            return result;
			}
        }, List.<Particle>nil()));
    }

    public void setAdditionalStep(F<Particle, Particle> additionalStep) {
        this.additionalStep = additionalStep;
    }

    public F<Particle, Particle> getAdditionalStep() {
        return additionalStep;
    }
}
