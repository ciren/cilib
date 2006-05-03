/*
 * Belief.java
 * 
 * Created on Aug 5, 2005
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

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author otter
 *
 * A Belief is also a type of Entity and it represent a piece of cultural information.
 */
@Deprecated
public class Belief implements Entity {
    
    protected String id;
    protected int dimension = 0;
    protected Type belief = null;    //represents the genetic structure.
    //Not needed...protected Fitness fitness; 
    
    //protected Random random = new MersenneTwister();    
    
    public Belief clone() {
        Belief clone = new Belief();
        //assigne a new ID, because in fact a new individual gets created.
        clone.id = CA.getNextBeliefID();
        //dimension
        clone.dimension = this.dimension;
        //genetic structure
        clone.belief = this.get().clone();
        
        return clone;
 }
 


    public Type get() {
        return this.belief;
    }
    
    public void set(Type type) {
    	this.belief = type;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    

    //public void initialise(OptimisationProblem problem, Initialiser i) {
    public void initialise(OptimisationProblem problem) {
        // ID initialization is done in the clone method...
        // which is always inforced due to the semantciss of the performInitialisation methods
        
    	//this.belief = DomainParser.getInstance().getInitialisedRepresenation();
    	//try {
    	this.belief = (Type) problem.getDomain().getBuiltRepresenation().clone();
    	this.belief.randomise();
    	//}
    	//catch (CloneNotSupportedException c) {
    		
    	//}
        this.dimension = ((MixedVector)this.belief).getDimension();
//        this.fitness = InferiorFitness.instance();        
    }

    public int getDimension() {
        return this.dimension;
    }


    public void setDimension(int dim) {
        this.dimension = dim;
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Population.Entity#compareTo(java.lang.Object)
     */
    public int compareTo(Entity o) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Population.Entity#setFitness(net.sourceforge.cilib.Problem.Fitness)
     */
    public void setFitness(Fitness fitness) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Population.Entity#getFitness()
     */
    public Fitness getFitness() {
        // TODO Auto-generated method stub
        return null;
    }



	public Type getBehaviouralParameters() {
		// TODO Auto-generated method stub
		return null;
	}



	public void setBehaviouralParameters(Type type) {
		// TODO Auto-generated method stub
		
	}
    
}
