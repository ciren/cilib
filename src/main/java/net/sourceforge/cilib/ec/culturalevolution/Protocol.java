/*
 * Protocol.java
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
 */
package net.sourceforge.cilib.ec.culturalevolution;

import java.util.Collection;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Topology;

/**
 * @author otter
 *
 * Defines an communicating and functioning interface between the population space and the belief space.
 * Every Protocol must implement this interface.
 * 
 * The implementations contains exact knowledge on how to adjust belief space and how to use it
 * to influence the variation of the population space.
 * 
 * Thus it's more than just a communication protocol it's a functioning protocol...
 * 
 * TODO: Find a better name for this class - to beter describe it.
 * 
 * Contains most of the problem specific stuff - the concrete implementations.
 * 
 * TODO: Proof of concept
 */
@Deprecated
public interface Protocol {
	
	//Determine which individuals from the current generation has an influence on the current beliefs
	public Collection accept(Topology<Individual> popspace);
	//The accepted (chosen) individuals is then used to update the belief space
	public void adjust(BeliefSpace beliefspace, Collection col);	//the implementations of this function will know exactly what to do with it.
	
	//send the EA over because variation is in conjunction with cross and mutation...
	public void variate(CA ca);
	//inside variate...
	//cross-over and mutation occurs (like normal EAOLD), but is under the influence() of the beliefs 
	
	//TODO: Think it will be better if we have this influence method implicit in the variate method???
	//public void influence();
}
