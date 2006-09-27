/*
 * EA.java
 * 
 * Created on Jun 22, 2005, 8:48:19 AM
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameterupdatestrategies.ConstantUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.ec.crossoveroperators.ArithmeticCrossOver;
import net.sourceforge.cilib.ec.crossoveroperators.CrossOverOperator;
import net.sourceforge.cilib.ec.mutationoperators.GaussianMutation;
import net.sourceforge.cilib.ec.mutationoperators.MutationOperator;
import net.sourceforge.cilib.ec.selectionoperators.Elitism;
import net.sourceforge.cilib.ec.selectionoperators.RandomSelection;
import net.sourceforge.cilib.ec.selectionoperators.SelectionOperator;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * @author otter
 * 
 * Basic Evolutionary Algorithm
 */
@Deprecated
public class EA extends PopulationBasedAlgorithm implements OptimisationAlgorithm /*, ParticipatingAlgorithm*/ {
	
	//Object fields...
	protected OptimisationProblem problem;
	protected Topology<Individual> population;
	protected Topology<Individual> offspring;
	protected int populationSize;
    
	protected Individual prototypeIndividual;
    protected Individual bestIndividual;
	
    protected Topology<Individual> topology;
	protected SelectionOperator<Individual> parentSelector;
	protected CrossOverOperator<Individual> crossover;
	protected MutationOperator<Individual> mutator;

    //used to vary the rate of mutation and cross-over
	protected ControlParameterUpdateStrategy mutationProbability;
	protected ControlParameterUpdateStrategy crossProbability;
	
    // The following is for purposes of merging the current generation with the offspring and generating the next generation.
    protected int generationGap;  //a generation gap of zero means that no parents will survive to the next generation.
    protected SelectionOperator<Individual> nextGenerationSurvivalSelector;  //selection operator that selects parents to survive
    protected SelectionOperator<Individual> nextGenerationOffSpringSelector; //selection operator that selects generated offspring for the next generation.

	//Static fields... These are used for identification purposes.
    protected static char currentPopulationId = 'A';
    protected static long currentIndividualId = 0;

    protected Random random;
    
    /**
     * Creates a new instance of <code>EA</code>. EA is a standard basic Evolutionary Algorithm general to all the subfields
     * within the Evolutionary Computation paradigm. 
     * 
     * All fields are initialised to reasonable defaults. 
     * 
     * Note that the {@link net.sourceforge.cilib.problem.OptimisationProblem}
     * is initially <code>null</code> and must be set before {@link #initialise()} is called.
     */
    public EA() {
    	super();
    	
        //This is all the default settings.
        //Changes can be made via the XML file.
        this.population = new StandardPopulation();
        this.offspring = new StandardPopulation();
        this.populationSize = 50;
        this.prototypeIndividual = new Individual();
        this.parentSelector = new RandomSelection<Individual>();
        this.nextGenerationSurvivalSelector = new RandomSelection<Individual>();
        this.crossover = new ArithmeticCrossOver<Individual>();
        this.mutator = new GaussianMutation<Individual>();
        this.mutationProbability = new ConstantUpdateStrategy(0.1);
        this.crossProbability = new ConstantUpdateStrategy(1.0); 
        this.random = new MersenneTwister();
    }    

    /**
     * Initialize the population and all the individuals within it.
     * 
     * The intiialization is based on the prototype individual as well as the domain string as specified in the XML file.
     * The type system is used to create the types and initialize them based on the domain.
     */
	protected void performInitialisation() {
        
        if (problem == null) {
            throw new InitialisationException("Usage error : No problem has been specified");
        }		
		//Set populatation ID.
		this.population.setId(String.valueOf(EA.getNextPopulationID()));
        //Set the prototype individual ID...
		prototypeIndividual.setId(EA.getNextIndividualID(this.population.getId()));
		
		//intialize all the individuals
        for (int i = 0; i < this.populationSize; ++i) {
            Individual indiv = prototypeIndividual.clone();
            indiv.initialise(problem);

            //add individual to the population
            this.population.add(indiv);
        }
        
        //this.printPopulation();
	}
	
	/**
	 * Implements the Template Method design pattern. This is the template method.
	 * The four sub steps of an evolutionary iteration takes on the form of primitive operations, which
	 * The task are delageted to the primitive operations's sub class implementations.
	 */
	protected void performIteration() {
		//1). determine fitness of each individual currently within the population
        performFitnessEvaluation(this.population);
        
        //2). cross-over.
        performCrossOver();
        
        //3). mutation.
        performMutation();
        
        //4). next generation formation.
        performNextGenerationFormation();
	}

	/**
	 * Primitive operation to evaluate the fitness of all the individuals within the supplied topology instance.
	 * @param PopoulationTopology
	 */
    protected void performFitnessEvaluation(Topology<Individual> pop) {
        //System.out.println("POPOPOPOP FORCE ALL FITNESS EVALUATIONS.");
        Iterator<Individual> iterator = pop.iterator();
        while (iterator.hasNext()) {
            //TODO: check casting hier...
          Individual individual = iterator.next();
          individual.setFitness(problem.getFitness(individual.get(),false));
        }
    }
    
	/**
	 * Primitive operation for the cross-over step within the performIteration template method.
	 */
    protected void performCrossOver() {
        //this.offspring.makeEmpty();
    	this.offspring.clear();
        Collection<Individual> parents = null;
        Collection<Individual> kiddies = null;
        //FIXME: What about the number of children needed?
        // Will a person always want to create a number of children equal to the generation size???
        //For now: create number of children equal to population size...
        while(this.offspring.size() < this.populationSize) {
        	//select parents. The number of parents needed is defined within the Cross over subclass being used.
        	parents = this.parentSelector.select(this.population,this.crossover.numberOffParentsNeeded());
            // based on a probabilty perform cross-overper to generate the new offspring.
            if (this.random.nextDouble() < this.crossProbability.getParameter()) {
            	kiddies = this.crossover.reproduce(parents);
            	
            	offspring.addAll(kiddies);
            }
        }    	
    }
    
	/**
	 * Primitive operation for the mutation step within the performIteration template method.
	 */
    protected void performMutation() {
     	// FIXME: Mutate the children, what about the parents??? As die kinders mutate as a funksie van die fitness, dan moet die fit
     	//ness eers weer uitgewerk word...
        //Base on a probability select individual to mutate...
        for(int i = 0; i < this.offspring.size(); i++) {
            if(random.nextDouble() < this.mutationProbability.getParameter()) {
        //		System.out.println("Before"+this.offspring.getIndividual(i).getID()+"\t"+ this.offspring.getIndividual(i).getFitness().getValue() +"\t"+this.offspring.getIndividual(i));
            	this.mutator.mutate(this.offspring.get(i));
        //		System.out.println("Afterr"+this.offspring.getIndividual(i).getID()+"\t"+ this.offspring.getIndividual(i).getFitness().getValue() +"\t"+this.offspring.getIndividual(i)+"\n");
        	}
        }
    }
    
	/**
	 * Primitive operation for the next generation formation step within the performIteration template method.
	 */
    protected void performNextGenerationFormation() {
        //      Merge the current population with the offspring population to get the next generation
        //
        // set the next generation with the individuals chosen from the current generation, according to the 
        // nextGenerationSurvivalSelector and the generationGap (number that survives to next generation).
    	List<Individual> tmp = this.nextGenerationSurvivalSelector.select(this.population,this.generationGap); 
        this.population.setAll(tmp);
        // Then fill up the next generation to full capacity by selecting individuals from the generated offspring using the
        // nextGenerationOffSpringSelector.
        tmp = this.nextGenerationOffSpringSelector.select(this.offspring,this.populationSize-this.generationGap);
        
        this.population.addAll(tmp);
    }
	
	/**
	 * Sets the optimisation problem to be solved.
	 *
	 * @param problem The {@link net.sourceforge.cilib.problem.OptimisationProblemAdapter} to be solved.
	 */
	public void setOptimisationProblem(OptimisationProblem problem) {
		this.problem = problem;
	}

	/**
	 * Accessor for the optimisation problem to be solved.
	 *
	 * @return The {@link net.sourceforge.cilib.problem.OptimisationProblemAdapter} to be solved.
	 */
	public OptimisationProblem getOptimisationProblem() {
	    return problem;
	}	
	
	/**
	  * Returns the best solution found by the algorithm so far.
	  *
	  * @return A double array of length {@link net.sourceforge.cilib.problem.OptimisationProblemAdapter#getDimension()} that represents the best solution found.
	  */
	public OptimisationSolution getBestSolution() {
        //System.out.println("GET BEST SOLUTION CALLED");
        //call getBestIndividual, which caculates the best solution thus far.
	    bestIndividual = getBestIndividual();
        return new OptimisationSolution(problem, bestIndividual.get());
	}	
	
	/**
	 * TODO: Multiple solution stuff...
     * Niching stuff...
	 */
	public java.util.Collection<OptimisationSolution> getSolutions() {
		System.out.println("GETSOLUTIONS CALLED");
		if(true)
			throw new RuntimeException("GETSOLUTIONS CALLED");
		
		ArrayList<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>(1);
		solutions.add(this.getBestSolution());
		return solutions;
	}
	
	public CrossOverOperator getCrossover() {
		return crossover;
	}
	public void setCrossover(CrossOverOperator<Individual> crossover) {
		this.crossover = crossover;
	}
	public ControlParameterUpdateStrategy getCrossProbability() {
		return crossProbability;
	}
	public void setCrossProbability(ControlParameterUpdateStrategy crossProbability) {
		this.crossProbability = crossProbability;
	}
	public ControlParameterUpdateStrategy getMutationProbability() {
		return mutationProbability;
	}
	public void setMutationProbability(ControlParameterUpdateStrategy mutationProbability) {
		this.mutationProbability = mutationProbability;
	}
	public MutationOperator<Individual> getMutator() {
		return mutator;
	}
	public void setMutator(MutationOperator<Individual> mutator) {
		this.mutator = mutator;
	}
	public SelectionOperator getParentSelector() {
		return parentSelector;
	}
	public void setParentSelector(SelectionOperator<Individual> parentSelector) {
		this.parentSelector = parentSelector;
	}
	public int getGenerationGap() {
		return generationGap;
	}
	public void setGenerationGap(int generationGap) {
		this.generationGap = generationGap;
	}
	public SelectionOperator getNextGenerationOffSpringSelector() {
		return nextGenerationOffSpringSelector;
	}
	public void setNextGenerationOffSpringSelector(
			SelectionOperator<Individual> nextGenerationOffSpringSelector) {
		this.nextGenerationOffSpringSelector = nextGenerationOffSpringSelector;
	}
	public SelectionOperator getNextGenerationSurvivalSelector() {
		return nextGenerationSurvivalSelector;
	}
	public void setNextGenerationSurvivalSelector(
			SelectionOperator<Individual> nextGenerationSurvivalSelector) {
		this.nextGenerationSurvivalSelector = nextGenerationSurvivalSelector;
	}	
	public Topology<Individual> getPopulation() {
		return population;
	}
	public void setPopulation(Topology<Individual> population) {
		this.population = population;
	}
	public int getPopulationSize() {
		return populationSize;
	}
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	public OptimisationProblem getProblem() {
		return problem;
	}
	public void setProblem(OptimisationProblem problem) {
		this.problem = problem;
	}
	public Individual getPrototypeIndividual() {
		return prototypeIndividual;
	}
	public void setPrototypeIndividual(Individual prototypeIndividual) {
		this.prototypeIndividual = prototypeIndividual;
	}	
    
	// Run through the population and get the best individual.
    // The best individual doesn't have to be calculated every generation, it is done only on request. As is not the case with the PSO stuff.
	public Individual getBestIndividual() {
        if (problem == null) {
            throw new RuntimeException("Optimisation problem not set - cannot find best individual");
        }
        // maak seker al die individuals se fitnesses is reg uitgewerk...
        this.performFitnessEvaluation(this.population);
        //get the best indiv, can use the elite operator with selection size of 1.
        SelectionOperator<Individual> se = new Elitism<Individual>();
        Collection tmp = null;
        tmp = se.select(this.population,1);
        //return bestIndividual;
        Iterator iter = tmp.iterator();
        return (Individual)iter.next();
	}
    
	public void setBestIndividual(Individual bestIndividual) {
		this.bestIndividual = bestIndividual;
	}
	public Topology<Individual> getOffspring() {
		return offspring;
	}
	public void setOffspring(Topology<Individual> offspring) {
		this.offspring = offspring;
	}
	public Random getRandom() {
		return random;
	}
	public void setRandom(Random random) {
		this.random = random;
	}	
    public static char getNextPopulationID() {
        return currentPopulationId++;
    }
    public static String getNextIndividualID(String popID) {
        return "" + popID + currentIndividualId++;
    }
    
    
    /**
     * Calculates the diameter of the population around the position of the best individual.
     * The diameter is calculated using the average Euclidean distance.
     *
     * @return The calculated diameter of the population.
     */
    public double getDiameter() {
        //double[] center = getBestParticle().getPosition();
    	Vector center = (Vector)this.getBestIndividual().get();
    	DistanceMeasure distance = new EuclideanDistanceMeasure();
        double diameter = 0;
        int count = 0;
        for (Iterator<Individual> i = this.population.iterator(); i.hasNext(); ++count) {
            Individual other = i.next();
            diameter += distance.distance(center, (Vector)other.get());
        }
        return diameter / count;
    }
    
    @Override
    public double getRadius() {
    	return 0;
    }
    
    /**
     * Helper function
     */
    public void printPopulation() {
        System.out.println("\n\n\n\nTESTING POPULATION\n\n\n");
        Iterator<Individual> iter = population.iterator();
        while (iter.hasNext()) {
            Individual indiv = iter.next();
            System.out.println(indiv.getId()+"\t"+indiv.getFitness().getValue()+"\t"+indiv.toString());
        }           
    }
    
    /**
     * Helper function
     */
    public void printChildren() {
        System.out.println("\n\n\n\nTESTING CHILDREN\n\n\n");
        Iterator<Individual> iter = offspring.iterator();
        while (iter.hasNext()) {
            Individual indiv = iter.next();
            System.out.println(indiv.getId()+"\t"+indiv.getFitness().getValue()+"\t"+indiv);
        }           
    }

    
    
	@Override
	public Topology<Individual> getTopology() {
		return this.topology;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setTopology(Topology topology) {
		Topology<Individual> top = (Topology<Individual>) topology;
		this.topology = top;		
	}
}
