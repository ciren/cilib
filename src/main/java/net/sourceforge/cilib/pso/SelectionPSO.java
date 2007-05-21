/*
 * SelectionPSO.java
 * 
 * Created on Jul 24, 2004
 *
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.pso;


public class SelectionPSO extends PSO {
	private static final long serialVersionUID = 2974661152550129709L;
	
	/*
	protected SelectionOperator<Particle> selector;
	protected int replacementSize;
	
	public SelectionPSO() {
		selector = new Elitism<Particle>();
		//half of the swarm.
		replacementSize = super.getParticles()/2;
	}
	
	// Correct with the needed iteration strategy
    public void performIteration() {
        for (Iterator<Particle> i = this.getTopology().iterator(); i.hasNext(); ) {
            Particle current = i.next();
            //current.setFitness(this.getOptimisationProblem().getFitness(current.getPosition(), true));
            current.calculateFitness();
            for (Iterator<Particle> j = this.getTopology().neighbourhood(i); j.hasNext(); ) {
                Particle other = j.next();
                if (current.getBestFitness().compareTo( other.getNeighbourhoodBest().getBestFitness()) > 0) {
                    other.setNeighbourhoodBest(current); // TODO: neighbourhood visitor?
                }
            }
        }
        
        /**
         * The performIteration semantics is the same, except for this part that comes in between, just before the 
         * velocity updates.
         *
        sortSwarm();
        replace(selector.select(this.getTopology(),this.replacementSize));
        /**
         * selection and position+velocity replacement completed. 
         *
        
        for (Iterator<Particle> i = this.getTopology().iterator(); i.hasNext(); ) {
            Particle current = i.next();
           // current.updateVelocity(this.getVelocityUpdate());      // TODO: replace with visitor (will simplify particle interface)
            current.updatePosition();                                        // TODO: replace with visitor (will simplify particle interface)
        }
    }	
	
	/**
	 * Sort the swarm, according to fitness.
	 *
	private void sortSwarm() {
        //copy the whole population to a candidate list.
        ArrayList<Particle> swarm = new ArrayList<Particle>(this.getTopology().getAll());
        //sort candidate list according to their fitnesses. Will do this automatically, because Entities is Comparable based on their fitness.
      	Collections.sort(swarm);
      	//replace the whole topology with the sorted swarm.
      	this.getTopology().setAll(swarm);
	}

	/**
	 * The best selected particles replaces the worst particles from the buttom up.
	 * Only the positions and velocities are updated, not the particles personal best.
	 * The semantics of the get() and set() methods are so that it works only on position.
     * getVelocity and setVelocity are used for purposes of particle's velocity.
	 * @pre Topology must be sorted from worst to bad...
	 * @param best
	 *
	private void replace(Collection<Particle> best) {
		int i = 0;
		
		for(Particle par : best){
			Vector velocity = (Vector) this.getTopology().get(i).getVelocity();
			Vector parVelocity = (Vector) par.getVelocity();
			
			this.getTopology().get(i).set(par.get());
			
			for (int k = 0; k < velocity.getDimension(); k++) {
				velocity.setReal(k, parVelocity.getReal(k));
			}
			i++;
		}
	}
	public int getReplacementSize() {
		return replacementSize;
	}
	public void setReplacementSize(int replacementSize) {
		this.replacementSize = replacementSize;
	}
	public SelectionOperator<Particle> getSelector() {
		return selector;
	}
	public void setSelector(SelectionOperator<Particle> selector) {
		this.selector = selector;
	}*/
}

