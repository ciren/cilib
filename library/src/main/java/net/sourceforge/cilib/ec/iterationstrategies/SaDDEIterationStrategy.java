/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.ec.ParameterizedIndividual;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.recipes.FeasibilitySelector;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/*
 * This is the Self-adaptive Diversity Differential Evolution iteration strategy described by Mezura-Montes 
 * and Palomeque-Ortiz in their 2009 paper "Self-adaptive and Deterministic Parameter
 * Control in Differential Evolution for Constrained Optimization". It is an adaptation 
 * of their previously described Diversity DE algorithm.
 * 
 * It adapts the three parameters, namely scaling factor, crossover probability and total number of
 * offspring, used by the DDE algorithm using the same process as generating a trial vector. The 
 * selector parameter used by the DDE algorithm is also adapteb, but in a different manner, slowly 
 * being decreased.
 * 
 * Further details about the article:
 * 
 * @incollection{Mezura09a,
 * author = {Efr\'{e}n Mezura-Montes and Ana Gabriela Palomeque-Ortiz},
 * title = {{Self-adaptive and Deterministic Parameter Control in Differential
 *	Evolution for Constrained Optimization}},
 * booktitle = {Constraint-Handling in Evolutionary Computation},
 * editor = {Efr\'{e}n Mezura-Montes},
 * pages = {95--120},
 * chapter = {5},
 * publisher = {Springer. Studies in Computational Intelligence, Volume 198},
 * address = {Berlin},
 * year = {2009},
 * note = {ISBN 978-3-642-00618-0}
}
 */

public class SaDDEIterationStrategy extends AbstractIterationStrategy<EC> {
    protected Selector targetVectorSelectionStrategy;
    private Selector parameterTargetVectorSelectionStrategy;
    private ProbabilityDistributionFunction selectorParameterRandom;
    private ProbabilityDistributionFunction lastSelectorFactorRandom;
    private ProbabilityDistributionFunction selectorRandom;
    private Selector offspringSelectionStrategy;
    private Selector nextGenerationSelectionStrategy;
    private Real selectorParameter;
    private double firstSelectorParameterValue;
    private double lastSelectorParameterValue;
    
    /*
     * Default constructor for SaDDEIterationStrategy
     */
    public SaDDEIterationStrategy() {
        this.targetVectorSelectionStrategy = new RandomSelector();
        this.parameterTargetVectorSelectionStrategy = new RandomSelector();
        this.selectorParameter = Real.valueOf(0.5, new Bounds(0.45,0.65));
        this.firstSelectorParameterValue = 0.5;
        this.lastSelectorParameterValue = 0.3;
        this.selectorParameterRandom = new UniformDistribution();
        ((UniformDistribution) selectorParameterRandom).setLowerBound(ConstantControlParameter.of(0.45));
        ((UniformDistribution) selectorParameterRandom).setUpperBound(ConstantControlParameter.of(0.65));
        
        this.lastSelectorFactorRandom = new UniformDistribution();
        ((UniformDistribution) lastSelectorFactorRandom).setLowerBound(ConstantControlParameter.of(0.0));
        ((UniformDistribution) lastSelectorFactorRandom).setUpperBound(ConstantControlParameter.of(0.5));
        
        this.selectorRandom = new UniformDistribution();
        ((UniformDistribution) selectorRandom).setLowerBound(ConstantControlParameter.of(0));
        ((UniformDistribution) selectorRandom).setUpperBound(ConstantControlParameter.of(1));
        
        offspringSelectionStrategy = new FeasibilitySelector<Individual>();
        nextGenerationSelectionStrategy = new FeasibilitySelector<Individual>();
    }
    
    /*
     * Copy constructor for SaDDEIterationStrategy
     * @param copy The SaDDEIterationStrategy to be copied
     */
    public SaDDEIterationStrategy(SaDDEIterationStrategy copy) {
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.parameterTargetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.selectorParameter = copy.selectorParameter;
        this.firstSelectorParameterValue = copy.firstSelectorParameterValue;
        this.lastSelectorParameterValue = copy.lastSelectorParameterValue;
        this.selectorParameterRandom = copy.selectorParameterRandom;
        this.selectorRandom = copy.selectorRandom;
        this.offspringSelectionStrategy = copy.offspringSelectionStrategy;
        this.nextGenerationSelectionStrategy = copy.offspringSelectionStrategy;
    }
    
    /*
     * Clone method for SaDDEIterationStrategy
     * @return A new instance of this SaDDEIterationStrategy
     */
    @Override
    public SaDDEIterationStrategy getClone() {
        return new SaDDEIterationStrategy(this);
    }

    /*
     * Performs an iteration of the Self-adaptive Diversity Differential Evolution algorithm, adapting the
     * individuals and the parameters held by them.
     * @param algorithm The algorithm on which to perform the iteration
     */
    @Override
    public void performIteration(EC algorithm) {
        Topology<ParameterizedIndividual> topology = (Topology<ParameterizedIndividual>) algorithm.getTopology();
        ArrayList<Individual> parameterList = new ArrayList<Individual>(); //
        ArrayList selectorList = new ArrayList();
        ParameterizedIndividual current; 
        ParameterizedIndividual offspringEntity;
        ParameterizedIndividual targetEntity;
        ParameterizedIndividual trialEntity;
        List<ParameterizedIndividual> offspring;
        ParameterizedIndividual tempOffspringEntity;
        Individual currentParameters; //
        
        if(AbstractAlgorithm.get().getIterations() == 1) {
            selectorParameter = Real.valueOf(selectorParameterRandom.getRandomNumber(), selectorParameter.getBounds());
            firstSelectorParameterValue = selectorParameter.doubleValue();
            lastSelectorParameterValue = lastSelectorFactorRandom.getRandomNumber();
        }
        
        for(int i = 0; i < topology.size(); i++) { //
            Individual individual = topology.get(i).getParameterHoldingIndividual();
            parameterList.add(individual);
        }
        
        Topology parameterTopology = new GBestTopology(); //
        parameterTopology.addAll(parameterList); //
        
        for (int i = 0; i < topology.size(); i++) {
            current = topology.get(i);
            currentParameters = current.getParameterHoldingIndividual(); //
            current.calculateFitness();
            offspringEntity = current.getClone();
            
            //System.out.println(current.getTotalOffspring());
            for(int o = 0; o < current.getTotalOffspring(); o++) {
                // Create the trial vector by applying mutation
                targetEntity = (ParameterizedIndividual) targetVectorSelectionStrategy.on(topology).exclude(current).select();
                Individual targetParameters = (Individual) parameterTargetVectorSelectionStrategy.on(parameterTopology).exclude(currentParameters).select(); //

                // Create the trial vector / entity
                trialEntity = (ParameterizedIndividual) current.getTrialVectorCreationStrategy().create(targetEntity.getClone(), current.getClone(), topology.getClone());
                Individual trialParameters = (Individual) targetEntity.getTrialVectorCreationStrategy().create(targetParameters.getClone(), currentParameters.getClone(), parameterTopology.getClone()); //

                // Create the offspring by applying cross-over
                offspring = (List<ParameterizedIndividual>) current.getCrossoverStrategy().crossover(Arrays.asList(current, trialEntity));// Order is VERY important here!!
                tempOffspringEntity = offspring.get(0);
                tempOffspringEntity.calculateFitness();
                
                //set the parameters of the tempOffspring
                if(((Vector) tempOffspringEntity.getCandidateSolution()).get(tempOffspringEntity.getDimension() - 1) ==  
                     ((Vector) trialEntity.getCandidateSolution()).get(trialEntity.getDimension() - 1)) {
                            tempOffspringEntity.setParameterHoldingIndividual(targetParameters.getClone());
                            
                } else {
                     tempOffspringEntity.setParameterHoldingIndividual(trialParameters.getClone());
                }
                
                selectorList.clear();
                selectorList.add(offspringEntity);
                selectorList.add(tempOffspringEntity);
                
                //Select the best offspring so far
                if(current.getTotalOffspring() > 1) {
                    offspringEntity = ((ParameterizedIndividual) offspringSelectionStrategy.on(selectorList).select()).getClone();
                } else {
                    offspringEntity = tempOffspringEntity.getClone();
                }
                
            }
            
            //Replace the Individual with the surviving individual
            if(selectorRandom.getRandomNumber() > selectorParameter.doubleValue()) {
                if(offspringEntity.getFitness().compareTo(current.getFitness()) > 0 ){
                    topology.set(i, offspringEntity.getClone());
                } 
            } else {
                selectorList.clear();
                selectorList.add(offspringEntity);
                selectorList.add(current);
                offspringEntity = (ParameterizedIndividual) nextGenerationSelectionStrategy.on(selectorList).select();
                topology.set(i, offspringEntity.getClone());
            }
            
        }
        
        //update the selector parameter
        updateSelectorParameter(AbstractAlgorithm.get().getIterations());
    }
    
    protected void updateSelectorParameter(int iterations) {
        double change = (firstSelectorParameterValue - lastSelectorParameterValue) / (double) iterations;
        lastSelectorParameterValue = selectorParameter.doubleValue();
        selectorParameter = Real.valueOf(selectorParameter.doubleValue() - change, selectorParameter.getBounds());
        
    }

    /*
     * Gets the Target Vector Selection Strategy
     * @return The target vector selection strategy
     */
    public Selector getTargetVectorSelectionStrategy() {
        return targetVectorSelectionStrategy;
    }

    /*
     * Sets the Target Vector Selection Strategy
     * @param targetVectorSelectionStrategy The new target vector selection strategy
     */
    public void setTargetVectorSelectionStrategy(Selector targetVectorSelectionStrategy) {
        this.targetVectorSelectionStrategy = targetVectorSelectionStrategy;
    }

     /*
     * Gets the Selector Random Probability Distribution Function
     * @return The Selector Random Probability Distribution Function
     */
    public ProbabilityDistributionFunction getSelectorRandom() {
        return selectorRandom;
    }

    /*
     * Sets the Selector Random Probability Distribution Function
     * @return The new Selector Random Probability Distribution Function
     */
    public void setSelectorRandom(ProbabilityDistributionFunction selectorRandom) {
        this.selectorRandom = selectorRandom;
    }

    /*
     * Gets the Offspring Selection Strategy
     * @return The Offspring Selection Strategy
     */
    public Selector getOffspringSelectionStrategy() {
        return offspringSelectionStrategy;
    }

    /*
     * Sets the Offspring Selection Strategy
     * @return The new Offspring Selection Strategy
     */
    public void setOffspringSelectionStrategy(Selector offspringSelectionStrategy) {
        this.offspringSelectionStrategy = offspringSelectionStrategy;
    }

    /*
     * Gets the Next Generation Selection Strategy
     * @return The Next Generation Selection Strategy
     */
    public Selector getNextGenerationSelectionStrategy() {
        return nextGenerationSelectionStrategy;
    }

    /*
     * Sets the Next Generation Selection Strategy
     * @return The new Next Generation Selection Strategy
     */
    public void setNextGenerationSelectionStrategy(Selector nextGenerationSelectionStrategy) {
        this.nextGenerationSelectionStrategy = nextGenerationSelectionStrategy;
    }

    /*
     * Gets the Selector Parameter
     * @return The Selector Parameter
     */
    public Real getSelectorParameter() {
        return selectorParameter;
    }

    /*
     * Sets the Selector Parameter
     * @return The new Selector Parameter
     */
    public void setSelectorParameter(Real selectorParameter) {
        this.selectorParameter = selectorParameter;
        firstSelectorParameterValue = selectorParameter.doubleValue();
    }

    /*
     * Gets the Selector Parameter Random Probability Distribution Function
     * @return The Selector Parameter Random Probability Distribution Function
     */
    public ProbabilityDistributionFunction getSelectorParameterRandom() {
        return selectorParameterRandom;
    }

    /*
     * Sets the Selector Parameter Random Probability Distribution Function
     * @return The new Selector Parameter Random
     */
    public void setSelectorParameterRandom(ProbabilityDistributionFunction selectorParameterRandom) {
        this.selectorParameterRandom = selectorParameterRandom;
    }

    /*
     * Gets the Last Selector Factor Random Probability Distribution Function
     * @return The Last Selector Factor Random Probability Distribution Function
     */
    public ProbabilityDistributionFunction getLastSelectorFactorRandom() {
        return lastSelectorFactorRandom;
    }

    /*
     * Sets the Last Selector Factor Random Probability Distribution Function
     * @return The new Last Selector Factor Random Probability Distribution Function
     */
    public void setLastSelectorFactorRandom(ProbabilityDistributionFunction lastSelectorFactorRandom) {
        this.lastSelectorFactorRandom = lastSelectorFactorRandom;
    }

    /*
     * Gets the First Selector Parameter Value
     * @return The First Selector Parameter Value
     */
    public double getFirstSelectorParameterValue() {
        return firstSelectorParameterValue;
    }

    /*
     * Sets the First Selector Parameter Value
     * @return The new First Selector Parameter Value
     */
    public void setFirstSelectorParameterValue(double firstSelectorParameterValue) {
        this.firstSelectorParameterValue = firstSelectorParameterValue;
    }

    /*
     * Gets the Last Selector Parameter Value
     * @return The Last Selector Parameter Value
     */
    public double getLastSelectorParameterValue() {
        return lastSelectorParameterValue;
    }

    /*
     * Sets the Last Selector Parameter Value
     * @return The new Last Selector Parameter Value
     */
    public void setLastSelectorParameterValue(double lastSelectorParameterValue) {
        this.lastSelectorParameterValue = lastSelectorParameterValue;
    }
    
}
