/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.comparator.RelaxedNonDominatedFitnessComparator;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;


public class RelaxedNonDominatedNeighbourhoodBestUpdateStrategy implements NeighbourhoodBestUpdateStrategy {
    
    @Override
    public NeighbourhoodBestUpdateStrategy getClone() {
        return this;
    }

    @Override
    public Fitness getSocialBestFitness(Entity entity) { 
        PSO pso = (PSO) AbstractAlgorithm.get();              
        Particle nBest = Topologies.getNeighbourhoodBest(pso.getTopology(), (Particle)entity, pso.getNeighbourhood(), new RelaxedNonDominatedFitnessComparator());
        return nBest.getBestFitness();
    }        
}
