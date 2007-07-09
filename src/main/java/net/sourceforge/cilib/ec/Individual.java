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
package net.sourceforge.cilib.ec;

import java.util.Map;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import net.sourceforge.cilib.util.calculator.VectorBasedFitnessCalculator;

/**
 * @author otter
 * Implements the Entity interface. Individual represents entities used within the EC paradigm.
 */
public class Individual extends AbstractEntity {
	private static final long serialVersionUID = -578986147850240655L;
	
	protected String id;
    protected int dimension = 0;
    protected Fitness fitness;
    protected FitnessCalculator fitnessCalculator;
    
    /**
     * Constructor
     */
    public Individual() {
        dimension = 0;
        this.properties.put("genes", new MixedVector());
        this.properties.put("penotypes", new MixedVector());
        fitness = InferiorFitness.instance();
        fitnessCalculator = new VectorBasedFitnessCalculator();
    }
    
    
    public Individual(Individual copy) {
        this.dimension = copy.dimension;
        this.fitness = InferiorFitness.instance();
        this.fitnessCalculator = copy.fitnessCalculator.clone();
        
        for (Map.Entry<String, Type> entry : copy.properties.entrySet()) {
        	String key = entry.getKey().toString();
    		this.properties.put(key, entry.getValue().clone());
        }
    }
    
    /**
     * Remember the semantics of clone here on the ID creates a new ID, so in fact it's a new chromosome
     * just with a copy of the genetic structure of the calling entity.
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
     public void initialise(OptimisationProblem problem) {
         // ID initialization is done in the clone method...
         // which is always inforced due to the semantciss of the performInitialisation methods         

    	 this.properties.put("genes", (Type) problem.getDomain().getBuiltRepresenation().clone());
    	 this.getContents().randomise();
    		 
    	 if (problem.getBehaviouralDomain().getBuiltRepresenation() != null) {
    		 this.properties.put("penotypes", problem.getBehaviouralDomain().getBuiltRepresenation().clone());
    		 this.properties.get("penotypes").randomise();
    	 }
    	 
    	 this.dimension = this.getContents().getDimension();
         this.fitness = InferiorFitness.instance();        
     }     
     
     /**
      * Compare to individuals fitness
      * See the semantics of the compare for fitnesses...
      */
    public int compareTo(Entity o) {
        return this.getFitness().compareTo(o.getFitness());
    }
    
    public Type getContents() {
        return this.properties.get("genes");
    }
    
    public void setContents(Type type) {
    	this.properties.put("genes", type);
    	this.dimension = type.getDimension();
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
    
    public void calculateFitness() {
    	calculateFitness(true);
    }
    
    public void calculateFitness(boolean count) {
        this.fitness = fitnessCalculator.getFitness(getContents(), count);
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setDimension(int dim) {
        this.dimension = dim;
    }
    
    public String toString() {
        StringBuffer str = new StringBuffer();
       
        str.append(getContents().toString());
        
        if (this.properties.get("penotypes") != null) {
        	str.append(" ");
        	str.append(this.properties.get("penotypes").toString());
        }
        
        return str.toString();
    }

    
    /**
	 * Return the <tt>Entity</tt> associated behavioural parameters
	 * @return a <tt>Type</tt> representing the behavioural parameters
	 */
	public Type getBehaviouralParameters() {
		return this.properties.get("penotypes");
	}

	
	/**
	 * Set the behavioural parameters for the <tt>Entity</tt>
	 * @param behaviouralParameters The behavioural parameters to set
	 */
	public void setBehaviouralParameters(Type type) {
		if (type instanceof Vector)
			this.properties.put("penotypes", type);
		else throw new RuntimeException("BehaviouralParameters need to be correct type! Please check and correct");
	}


	public void reinitialise() {
		// TODO Auto-generated method stub
		
	}    
}
