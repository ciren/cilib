/*
 * Individual.java
 *
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.ec;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Type;
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
    protected FitnessCalculator fitnessCalculator;
    
    /**
     * Create an instance of {@linkplain Individual}.
     */
    public Individual() {
        dimension = 0;
        setContents(new Vector());
        this.properties.put("penotypes", new Vector());
        this.properties.put("fitness", InferiorFitness.instance());
        fitnessCalculator = new VectorBasedFitnessCalculator();
    }
    
    /**
     * Copy constructor. Creates a copy of the given {@linkplain Individual}.
     * @param copy The {@linkplain Individual} to copy.
     */
    public Individual(Individual copy) {
    	super(copy);
        this.dimension = copy.dimension;
        this.fitnessCalculator = copy.fitnessCalculator.getClone();
    }
    
    /**
     * {@inheritDoc}
     */
     public Individual getClone() {
    	 return new Individual(this);
     }

	/**
      * Resets the fitness to <code>InferiorFitness</code>.
      */
     public void resetFitness() {
         this.properties.put("fitness", InferiorFitness.instance());
     }     

     /**
      * {@inheritDoc}
      */
     public void initialise(OptimisationProblem problem) {
         // ID initialization is done in the clone method...
         // which is always enforced due to the semantics of the performInitialisation methods         

    	 this.setContents((Type) problem.getDomain().getBuiltRepresenation().getClone());
    	 this.getContents().randomise();
    		 
    	 if (problem.getBehaviouralDomain().getBuiltRepresenation() != null) {
    		 this.properties.put("penotypes", problem.getBehaviouralDomain().getBuiltRepresenation().getClone());
    		 this.properties.get("penotypes").randomise();
    	 }
    	 
    	 this.dimension = this.getContents().getDimension();
    	 this.properties.put("fitness", InferiorFitness.instance());
     }     
     
     /**
      * {@inheritDoc} 
      */
     public int compareTo(Entity o) {
    	 return this.getFitness().compareTo(o.getFitness());
     }
    
     /**
      * {@inheritDoc}
      */
     public void setContents(Type type) {
    	 super.setContents(type);
    	 this.dimension = type.getDimension();
     }

     /**
      * Get the identifier assigned to this {@linkplain Individual}.
      * @return The assigned identifier.
      */
    public String getId() {
        return id;
    }

    /**
     * Set the identifier for this {@linkplain Individual}.
     * @param id The identifier to assign to the {@linkplain Individual}.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    public void calculateFitness() {
    	calculateFitness(true);
    }
    
    /**
     * {@inheritDoc}
     */
    public void calculateFitness(boolean count) {
    	this.properties.put("fitness", fitnessCalculator.getFitness(getContents(), count));
    }

    /**
     * {@inheritDoc}
     */
    public int getDimension() {
        return this.dimension;
    }

    /**
     * Set the current dimension value for the current {@linkplain Individual}.
     * @param dim The dimension value to set.
     */
    public void setDimension(int dim) {
        this.dimension = dim;
    }
    
    /**
     * Create a textual representation of the current {@linkplain Individual}. The
     * returned {@linkplain String} will contain both the genotypes and penotypes for
     * the current {@linkplain Individual}.
     * @return The textual representation of this {@linkplain Individual}.
     */
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
	 * Return the <tt>Entity</tt> associated behavioural parameters.
	 * @return a <tt>Type</tt> representing the behavioural parameters.
	 */
	public Type getBehaviouralParameters() {
		return this.properties.get("penotypes");
	}

	
	/**
	 * Set the behavioural parameters for the <tt>Entity</tt>.
	 * @param type The behavioural parameters to set.
	 */
	public void setBehaviouralParameters(Type type) {
		if (type instanceof Vector)
			this.properties.put("penotypes", type);
		else throw new RuntimeException("BehaviouralParameters need to be correct type! Please check and correct");
	}

	/**
	 * {@inheritDoc}
	 */
	public void reinitialise() {
		throw new UnsupportedOperationException("Implementation is required for this method");
	}    
}
