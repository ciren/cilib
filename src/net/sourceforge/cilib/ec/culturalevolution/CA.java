/*
 * CE.java
 * 
 * Created on Aug 1, 2005
 * 
 * Copyright (C) 2003, 2004, 2005 - CIRG@UP 
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
 * 
 */
package net.sourceforge.cilib.ec.culturalevolution;

import java.util.Iterator;

import net.sourceforge.cilib.ec.ea.EA;

/**
 * @author otter
 *
 * CE - Cultural Evolution
 */
public class CA extends EA {
	
	protected Protocol comProt = null;
	protected BeliefSpace beliefSpace = null;
	protected int beliefSpaceSize = 0;
	protected Belief prototypeBelief = null;
	//the population space and everything relative to the EAOLD used is implicit in the inheritance...
    protected static long currentBeliefId = 0;
	
	public CA () {
		//comProt = new //ConcreteProtocol...
		this.beliefSpace = new BeliefSpace();
		this.beliefSpaceSize = 1;
		this.prototypeBelief = new Belief(); 
	}
	
	
	public void performInitialisation() {
		//initialize the genetic population... 
		super.performInitialisation();
		
		//initialize the belief space...
		prototypeBelief.setId(CA.getNextBeliefID());	//small letter b stands for belief	
		
        for (int i = 0; i < this.beliefSpaceSize; ++i) {
            Belief belief = null;
            //try {
            	belief = prototypeBelief.clone();
            //}
            //catch (CloneNotSupportedException ex) {
            //    throw new InitialisationException("Could not clone prototype belief");
           // }
            //add belief to belief space
            this.beliefSpace.add(belief);
        }
        
      //  this.printBeliefs();
	}
	
	/**
	 * This method is still a template method. The fitness eval and next generation formation primitive methods
	 * of the EA base class is called, but cross-over and mutation is done within the defined protocol instance.
	 */
	public void performIteration() {
		
		//1). determine fitness of each individual currently within the population
        this.performFitnessEvaluation(this.population);
        
        //2). accept some individuals into belief space and adjust the belief space
        comProt.adjust(this.beliefSpace, this.comProt.accept(this.population));
        
        //3). variate(evolve) the population space using the beliefs as an influence
        comProt.variate(this);
        
        //4). select new generation...
        this.performNextGenerationFormation();
	}
    
    //small letter b, stands for belief...
    public static String getNextBeliefID() {
        return "" + 'b' + currentBeliefId++;
    }
    
	public void printBeliefs() {
        Iterator<Belief> iter = this.beliefSpace.iterator();
        while (iter.hasNext()) {
            Belief belief = (Belief)iter.next();
            System.out.println(belief.getId()+"\t"+"\t"+belief.toString());
        }        
	}
}
