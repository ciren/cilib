/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.ec.ParameterizedDEIndividual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.problem.boundaryconstraint.ParameterBoundaryConstraint;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 *
 * @author Kristina
 */
public class DifferentialEvolutionParameterizedIterationStrategy extends AbstractIterationStrategy<EC>{
    private Selector targetVectorSelectionStrategy; // x
    private CreationStrategy trialVectorCreationStrategy; // y
    private CrossoverStrategy crossoverStrategy; // z
    
    private Selector parameterTargetVectorSelectionStrategy;
    private CreationStrategy parameterTrialVectorCreationStrategy; 
    private CrossoverStrategy parameterCrossoverStrategy; 

    /**
     * Create an instance of the {@linkplain DifferentialEvolutionParameterizedIterationStrategy}.
     */
    public DifferentialEvolutionParameterizedIterationStrategy() {
        this.targetVectorSelectionStrategy = new RandomSelector();
        this.trialVectorCreationStrategy = new RandCreationStrategy();
        this.crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
        
        this.parameterTargetVectorSelectionStrategy = new RandomSelector();
        this.parameterTrialVectorCreationStrategy = new RandCreationStrategy();
        this.parameterCrossoverStrategy = new DifferentialEvolutionBinomialCrossover();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public DifferentialEvolutionParameterizedIterationStrategy(DifferentialEvolutionParameterizedIterationStrategy copy) {
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.trialVectorCreationStrategy = copy.trialVectorCreationStrategy.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        
        this.parameterTargetVectorSelectionStrategy = copy.parameterTargetVectorSelectionStrategy;
        this.parameterTrialVectorCreationStrategy = copy.parameterTrialVectorCreationStrategy.getClone();
        this.parameterCrossoverStrategy = copy.parameterCrossoverStrategy.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DifferentialEvolutionParameterizedIterationStrategy getClone() {
        return new DifferentialEvolutionParameterizedIterationStrategy(this);
    }

    /**
     * Perform an iteration of the DE algorithm defined as the DE/x/y/z implementation.
     * This implementation is specific to ParameterizedDEIndividuals and updates the 
     * parameters as well as the candidate solutions.
     * @param ec The {@linkplain EC} on which to perform this iteration.
     */
    @Override
    public void performIteration(EC ec) {
        @SuppressWarnings("unchecked")
        Topology<Entity> topology = (Topology<Entity>) ec.getTopology();

        for (int i = 0; i < topology.size(); i++) {
            Entity current = topology.get(i);
            Entity parameterCurrent = buildParameterEntity((ParameterizedDEIndividual) current);
            current.calculateFitness();

            // Create the trial vector by applying mutation
            Entity targetEntity = (Entity) targetVectorSelectionStrategy.on(topology.getClone()).exclude(current).select();
            Entity targetParameterEntity = buildParameterEntity((ParameterizedDEIndividual) targetVectorSelectionStrategy.on(topology.getClone()).exclude(current).select());

            // Create the trial vector / entity
            trialVectorCreationStrategy.setControlParameters(targetEntity);
            parameterTrialVectorCreationStrategy.setControlParameters(targetEntity);
            
            Entity trialEntity = trialVectorCreationStrategy.create( targetEntity, current, topology);
            Entity parameterTrialEntity = parameterTrialVectorCreationStrategy.create(targetParameterEntity, parameterCurrent, buildParameterTopology(topology));
            
            // Create the offspring by applying cross-over
            crossoverStrategy.setControlParameters(trialEntity);
            parameterCrossoverStrategy.setControlParameters(trialEntity);
            List<Entity> offspring = this.crossoverStrategy.crossover(Arrays.asList(current, trialEntity)); // Order is VERY important here!!
            List<Entity> parameterOffspring = this.parameterCrossoverStrategy.crossover(Arrays.asList(parameterCurrent, parameterTrialEntity));
            
            // Replace the parent (current) if the offspring is better
            Entity offspringEntity;
            Entity parameterOffspringEntity;
            
            if(!offspring.isEmpty())
                offspringEntity = offspring.get(0);
            else 
                offspringEntity = targetEntity.getClone();
            
            
            if(!parameterOffspring.isEmpty())
                parameterOffspringEntity = parameterOffspring.get(0);
            else 
                parameterOffspringEntity = targetParameterEntity.getClone();
           
            
            ParameterBoundaryConstraint parameterBoundaryConstraint = new ParameterBoundaryConstraint();
            parameterBoundaryConstraint.setBoundaryConstraint(boundaryConstraint);
            
            ParameterizedDEIndividual resultingIndividual = buildResultingEntity(offspringEntity, (ParameterizedDEIndividual) targetEntity, parameterOffspringEntity);
            
            boundaryConstraint.enforce(offspringEntity);
            parameterBoundaryConstraint.enforce(resultingIndividual);
            
            resultingIndividual.calculateFitness();

            if (resultingIndividual.getFitness().compareTo(current.getFitness()) > 0) { // the trial vector is better than the parent
                topology.set(i, resultingIndividual); // Replace the parent with the offspring individual
            }
        }
    }
    
    /*
     * Creates the final entity by combining the candidate solution offspring with the parameter offspring.
     * @param offspringEntity The offspring resulting from the candidate solutions
     * @param targetEntity The current target individual
     * @param parameterOffspringEntity The offspring resulting from the parameter vector
     * @return resultingIndividual The complete ParameterizedDEIndividual.
     */
    private ParameterizedDEIndividual buildResultingEntity(Entity offspringEntity,ParameterizedDEIndividual targetEntity, Entity parameterOffspringEntity) {
        ParameterizedDEIndividual resultingIndividual = new ParameterizedDEIndividual();
        resultingIndividual.setCandidateSolution(offspringEntity.getCandidateSolution());

        if(targetEntity.getScalingFactor() instanceof BoundedModifiableControlParameter) {
            BoundedModifiableControlParameter parameter = (BoundedModifiableControlParameter) targetEntity.getScalingFactor();
            resultingIndividual.setScalingFactor(new BoundedModifiableControlParameter(((Vector)parameterOffspringEntity.getCandidateSolution()).get(0).doubleValue(),
                    parameter.getLowerBound(), parameter.getUpperBound()));

        } else {
            ConstantControlParameter parameter = (ConstantControlParameter) targetEntity.getScalingFactor();
            resultingIndividual.setScalingFactor(ConstantControlParameter.of(((Vector)parameterOffspringEntity.getCandidateSolution()).get(0).doubleValue()));
        }

        if(targetEntity.getRecombinationProbability() instanceof BoundedModifiableControlParameter) {
            BoundedModifiableControlParameter parameter = (BoundedModifiableControlParameter) targetEntity.getRecombinationProbability();
            resultingIndividual.setRecombinationProbability(new BoundedModifiableControlParameter(((Vector)parameterOffspringEntity.getCandidateSolution()).get(1).doubleValue(),
                    parameter.getLowerBound(), parameter.getUpperBound()));

        } else {
            ConstantControlParameter parameter = (ConstantControlParameter) targetEntity.getRecombinationProbability();
            resultingIndividual.setRecombinationProbability(ConstantControlParameter.of(((Vector)parameterOffspringEntity.getCandidateSolution()).get(1).doubleValue()));
        }
        
        return resultingIndividual;
    }
    
    /*
     * Creates a topology of individuals which contain the parameters as their candidate solutions.
     * @param topology The current individual topology
     * @return parameterTopology The new topology consisting of parameters
     */
    private Topology buildParameterTopology(Topology<Entity> topology) {
        Topology parameterTopology = topology.getClone();
        parameterTopology.clear();
        Entity individual = new Individual();
        
        for (Entity entity : topology) {
            individual = new Individual();
            ParameterizedDEIndividual parameterizedEntity = (ParameterizedDEIndividual) entity;
            individual.setCandidateSolution(Vector.of(parameterizedEntity.getScalingFactor().getParameter(), parameterizedEntity.getRecombinationProbability().getParameter()));
              
            parameterTopology.add(individual);
        }
        
        return parameterTopology;
    }
    
    /*
     * 
     */
    private Entity buildParameterEntity(ParameterizedDEIndividual individual) {
        Entity entity = new Individual();
        entity.setCandidateSolution(Vector.of(individual.getScalingFactor().getParameter(), individual.getRecombinationProbability().getParameter()));
        
        return entity;
    }

    /**
     * Obtain the {@linkplain SelectionStrategy} used to select the target vector.
     * @return The {@linkplain SelectionStrategy} of the target vector.
     */
    public Selector getTargetVectorSelectionStrategy() {
        return targetVectorSelectionStrategy;
    }

    /**
     * Set the {@linkplain SelectionStrategy} used to select the target vector within the DE.
     * @param targetVectorSelectionStrategy The {@linkplain SelectionStrategy} to use for the
     *        selection of the target vector.
     */
    public void setTargetVectorSelectionStrategy(Selector targetVectorSelectionStrategy) {
        this.targetVectorSelectionStrategy = targetVectorSelectionStrategy;
    }

    /**
     * Get the {@linkplain CrossoverStrategy} used to create offspring entities.
     * @return The {@linkplain CrossoverStrategy} used to create offspring.
     */
    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    /**
     * Set the {@linkplain CrossoverStrategy} used to create offspring entities.
     * @param crossoverStrategy The {@linkplain CrossoverStrategy} to create entities.
     */
    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    /**
     * Get the current strategy for creation of the trial vector.
     * @return The {@linkplain CreationStrategy}.
     */
    public CreationStrategy getTrialVectorCreationStrategy() {
        return trialVectorCreationStrategy;
    }

    /**
     * Set the strategy to create trial vectors.
     * @param trialVectorCreationStrategy The value to set.
     */
    public void setTrialVectorCreationStrategy(CreationStrategy trialVectorCreationStrategy) {
        this.trialVectorCreationStrategy = trialVectorCreationStrategy;
    }
    
    public Selector getParameterTargetVectorSelectionStrategy() {
        return parameterTargetVectorSelectionStrategy;
    }

    /**
     * Set the {@linkplain SelectionStrategy} used to select the target vector within the DE.
     * @param targetVectorSelectionStrategy The {@linkplain SelectionStrategy} to use for the
     *        selection of the target vector.
     */
    public void setParameterTargetVectorSelectionStrategy(Selector targetVectorSelectionStrategy) {
        this.parameterTargetVectorSelectionStrategy = targetVectorSelectionStrategy;
    }

    /**
     * Get the {@linkplain CrossoverStrategy} used to create offspring entities.
     * @return The {@linkplain CrossoverStrategy} used to create offspring.
     */
    public CrossoverStrategy getParameterCrossoverStrategy() {
        return parameterCrossoverStrategy;
    }

    /**
     * Set the {@linkplain CrossoverStrategy} used to create offspring entities.
     * @param crossoverStrategy The {@linkplain CrossoverStrategy} to create entities.
     */
    public void setParameterCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.parameterCrossoverStrategy = crossoverStrategy;
    }

    /**
     * Get the current strategy for creation of the trial vector.
     * @return The {@linkplain CreationStrategy}.
     */
    public CreationStrategy getParameterTrialVectorCreationStrategy() {
        return parameterTrialVectorCreationStrategy;
    }

    /**
     * Set the strategy to create trial vectors.
     * @param trialVectorCreationStrategy The value to set.
     */
    public void setParameterTrialVectorCreationStrategy(CreationStrategy trialVectorCreationStrategy) {
        this.parameterTrialVectorCreationStrategy = trialVectorCreationStrategy;
    }
}
