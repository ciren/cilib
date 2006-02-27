/*
 * Individual.java
 * 
 * Created on Jun 21, 2005
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
package net.sourceforge.cilib.ec.ea;

import java.util.Random;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.MersenneTwister;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;

/**
 * @author otter
 * Implements the Entity interface. Individual represents entities used within the EC paradigm.
 */
public class Individual implements Entity {
    
    protected String id;
    protected int dimension = 0;
    protected Type genes = null;    //represents the genetic structure.
    protected Type penotypes = null;
    protected Fitness fitness; 
    
    protected Random random = new MersenneTwister();
    
    /**
     * Constructor
     */
    public Individual() {
        dimension = 0;
        genes = new MixedVector();
        penotypes = new MixedVector();
        fitness = InferiorFitness.instance();
    }
    
    
    public Individual(Individual copy) {
        //assigne a new ID, because in fact a new individual gets created.
        this.id = EA.getNextIndividualID(String.valueOf(copy.getId().charAt(0)));
        //dimension
        this.dimension = copy.dimension;
        //genetic structure
        this.genes = copy.genes.clone();
        this.penotypes = copy.penotypes.clone();
        //fitness - word aoutomaties oor geskakel, maar maak reset dit eerder
        this.resetFitness();
    }
    
    /**
     * Remember the semantics of clone here on the ID creates a new ID, so in fact it's a new chromosome
     * just with a copy of the genetic structure of the calling entity.
     * 
     * Take note : the fitness of the clone is reset to <code>InferiorFitness</code>
     */
     public Individual clone() {
    	 return new Individual(this);
     }
     
     /**
      * Resets the fitness to <code>InferiorFitness</code>
      */
     public void resetFitness() {
         this.fitness = InferiorFitness.instance();
     }     

     /**
      * Intialise Individual according to the problem domain. The Type System handles it for us
      * and thus TODO: intialise must fall away.
      */
     //public void initialise(OptimisationProblem problem, Initialiser i) {
     public void initialise(OptimisationProblem problem) {
         // ID initialization is done in the clone method...
         // which is always inforced due to the semantciss of the performInitialisation methods         

    	 //this.genes = DomainParser.getInstance().getInitialisedRepresenation();
    	 
    	 //try {
    		 this.genes = (Type) problem.getDomain().getBuiltRepresenation().clone();
    		 this.genes.randomise();
    		 
    		 // TODO: Must do this in a better way!
    		 if (problem.getBehaviouralDomain().getBuiltRepresenation() != null) {
    			 this.penotypes = (Type) problem.getBehaviouralDomain().getBuiltRepresenation().clone();
    			 this.penotypes.randomise();
    		 }
    	 //}
    	 //catch (CloneNotSupportedException e) {
    	//	 throw new RuntimeException(e);
    	 //}
    	 
         this.dimension = ((MixedVector)this.genes).getDimension();
         this.fitness = InferiorFitness.instance();        
     }     
     
     /**
      * Compare to individuals fitness
      * See the semantics of the compare for fitnesses...
      */
    public int compareTo(Entity o) {
        return this.getFitness().compareTo(o.getFitness());
    }
    
    //get the genetic content of the Individual.
    public Type get() {
        return genes;
    }
    
    public void set(Type type) {
    	this.genes = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Fitness getFitness() {
        return fitness;
    }
    
    public void setFitness(Fitness fitness) {
        this.fitness = fitness;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setDimension(int dim) {
        this.dimension = dim;
    }
    
    public String toString() {
        StringBuffer str = new StringBuffer();
        //str = this.ID + "\t" + this.fitness.getValue() + "\t";
        str.append(this.genes.toString());
        
        if (this.penotypes != null) {
        	str.append(" ");
        	str.append(this.penotypes.toString());
        }
        
        return str.toString();
    }

    
    /**
	 * Return the <tt>Entity</tt> associated behavioural parameters
	 * @return a <tt>Type</tt> representing the behavioural parameters
	 */
	public Type getBehaviouralParameters() {
		return this.penotypes;
	}

	
	/**
	 * Set the behavioural parameters for the <tt>Entity</tt>
	 * @param behaviouralParameters The behavioural parameters to set
	 */
	public void setBehaviouralParameters(Type type) {
		if (type instanceof Vector)
			this.penotypes = (Vector) type;
		else throw new RuntimeException("BehaviouralParameters need to be correct type! Please check and correct");
	}    
}
