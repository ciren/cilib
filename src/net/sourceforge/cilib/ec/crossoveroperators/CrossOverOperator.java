/*
 * CrossOverOperator.java
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
package net.sourceforge.cilib.ec.crossoveroperators;

import java.util.Collection;

import net.sourceforge.cilib.entity.Entity;

/**
 * @author otter
 *
 * Defines a common interface to all crossover operators.
 * 
 * Every cross-over operator must implement this interface.
 * The collection of parents is passed when the reproduce method is executed and 
 * a collection of offpsring is returned.
 * 
 * The classes that implements this interface will know exactly what
 * to do with the parent collection, cause it is defined within them.
 * 
 * The number of parents needed and number of offspring generated is defined within the
 * different cross-over operator implementations itself.
 * 
 * Sub-class constructor must assign suitable values to the number of offspring and number of parents.
 * If applicable for this numbers to change - make set methods available so that user can
 * define the numbers via the configuration XML file.
 */
public abstract class CrossOverOperator<E extends Entity> {
    
    protected int offspring;
    protected int parents;
    
    /**
     * Receives a collection of parents and returns a collection of offspring
     * @param parents.
     * @return offspring.
     */
	public abstract Collection<E> reproduce(Collection<? extends E> parents);	
	
    
	/**
	 * Returns the number of parents needed.
	 * @return int - number of parents needed.
	 */
	public int numberOffParentsNeeded() {
        return parents;        
    }
	/**
	 * Returns the number of offspring which the operator will generate everytime needed.
	 * @return int - number of offspring generated.
	 */
	public int numberOffChildrenGenerated() {
        return offspring;        
    }
}
