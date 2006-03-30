package net.sourceforge.cilib.pso.niching;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * 
 *@author 'Segun
 *
 */

public class GBestAbsorptionStrategy<E extends PopulationBasedAlgorithm> implements AbsorptionStrategy<E> {
    
    public void absorb(E mainSwarm, Collection<? extends E> subSwarms) {
        
        Iterator mainSwarmIterator = mainSwarm.getTopology().iterator();
        
        while(mainSwarmIterator.hasNext()) {
            Particle mainSwarmParticle = (Particle)mainSwarmIterator.next();
            
            Iterator subSwarmsIterator = subSwarms.iterator();
            
            while(subSwarmsIterator.hasNext()) {
                PSO subSwarm = (PSO)subSwarmsIterator.next();
                double subSwarmRadius = subSwarm.getRadius();
                
                Particle subSwarmBestParticle = subSwarm.getBestParticle();
                Vector subSwarmBestParticlePosition = (Vector)subSwarmBestParticle.getPosition();
                Vector mainSwarmParticlePosition = (Vector)mainSwarmParticle.getPosition();
                
                DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
                double distance = distanceMeasure.distance(subSwarmBestParticlePosition, mainSwarmParticlePosition);
                
                if(subSwarmRadius >= distance) {
                    subSwarm.getTopology().add(mainSwarmParticle);
                    // mainSwarm.getTopology().getAll().remove(mainSwarmParticle); // remove the sub-swarm absorbed particle from the main swarm
                }
                
            }
            
        }
        
    }
    
}
