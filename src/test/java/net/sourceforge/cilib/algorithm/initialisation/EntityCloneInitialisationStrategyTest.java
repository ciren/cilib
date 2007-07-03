/*
 * EntityCloneInitialisationBuilderTest.java
 *
 * Created on April 24, 2006, 2:26 PM
 *
 *
 * Copyright (C) 2003 - 2006 
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
package net.sourceforge.cilib.algorithm.initialisation;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.functions.continuous.Rastrigin;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.particle.StandardParticle;

import org.junit.Test;

/**
 * Class to test the concept of initialising a <tt>Topology</tt> of
 * <tt>Entity</tt> objects, by cloning a given prototype <tt>Entity</tt>.
 *  
 * @author Gary Pampara
 */
public class EntityCloneInitialisationStrategyTest {
	
	@Test
	public void intialiseClonedParticleTopology() {
		Topology<Particle> topology = new GBestTopology<Particle>();
		
		FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
		problem.setFunction(new Rastrigin());
		
		InitialisationStrategy initialisationBuilder = new ClonedEntityInitialisationStrategy();
		initialisationBuilder.setEntityType(new StandardParticle());
		initialisationBuilder.setNumberOfEntities(40); // 40 Particles
		
		initialisationBuilder.initialise(topology, problem);
		
		assertEquals(40, topology.size());
		
		// Test for object uniqueness
		Set<Particle> set = new HashSet<Particle>();
		for (Particle particle : topology) {
			set.add(particle);
		}
		
		assertEquals(40, set.size());
	}
	

	@Test
	public void intialiseClonedIndividualTopology() {
		Topology<Individual> population = new GBestTopology<Individual>();
		
		FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
		problem.setFunction(new Rastrigin());
		
		InitialisationStrategy initialisationBuilder = new ClonedEntityInitialisationStrategy();
		initialisationBuilder.setEntityType(new Individual());
		initialisationBuilder.setNumberOfEntities(40); // 40 Individuals
		
		initialisationBuilder.initialise(population, problem);
		
		assertEquals(40, population.size());
		
		// Test for object uniqueness
		Set<Individual> set = new HashSet<Individual>();
		for (Individual individual : population) {
			set.add(individual);
		}
		
		assertEquals(40, set.size());
	}

}
