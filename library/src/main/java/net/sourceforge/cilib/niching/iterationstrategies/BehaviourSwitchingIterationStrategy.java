/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.niching.iterationstrategies;

import fj.data.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.niching.NichingAlgorithm;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;

/**
 *
 * @author filipe
 */
public class BehaviourSwitchingIterationStrategy extends AbstractIterationStrategy<NichingAlgorithm> {
    
    private PSO pso = new PSO();
    private IterationStrategy<NichingAlgorithm> delegate;
    private ControlParameter switchingIteration;
    private StandardParticleBehaviour behaviour;

    @Override
    public AbstractIterationStrategy getClone() {
        return this;
    }

    @Override
    public void performIteration(NichingAlgorithm algorithm) {
        if (algorithm.getIterations() == ((int) switchingIteration.getParameter())) {
            List<? extends Entity> topology = algorithm.getTopology();
            for (SinglePopulationBasedAlgorithm pba : algorithm.getPopulations()) {
                topology = topology.append(pba.getTopology());
            }
            
            for (Entity p : topology) {
                ((Particle)p).setBehaviour(behaviour);
            }
            
            pso.setOptimisationProblem(algorithm.getOptimisationProblem());
            pso.performInitialisation();
            pso.setTopology((List<Particle>)topology);
            algorithm.setMainSwarm(pso);
            algorithm.setPopulations(new java.util.ArrayList<SinglePopulationBasedAlgorithm>());
        }
        
        if (algorithm.getIterations() >= ((int) switchingIteration.getParameter())) {
            algorithm.getMainSwarm().performIteration();
            return;
        }
        
        delegate.performIteration(algorithm);
    }

    public void setPso(PSO pso) {
        this.pso = pso;
    }

    public void setSwitchingIteration(ControlParameter switchingIteration) {
        this.switchingIteration = switchingIteration;
    }

    public void setDelegate(IterationStrategy<NichingAlgorithm> delegate) {
        this.delegate = delegate;
    }

    public void setBehaviour(StandardParticleBehaviour behaviour) {
        this.behaviour = behaviour;
    }
    
}
