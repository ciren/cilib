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
package net.sourceforge.cilib.ec;

import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ParameterAdaptingControlParameter;
import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Sequence;

/**
 * This class extends the standard individual, allowing for an 
 * individual to hold the two parameters: scaling factor and 
 * recombination probability.
 */
public class ParameterizedDEIndividual extends Individual{

    private ParameterAdaptingControlParameter scalingFactor;
    private ParameterAdaptingControlParameter recombinationProbability;
    private ProbabilityDistributionFuction random;
    
    public ParameterizedDEIndividual() {
        this.setCandidateSolution(new Vector());
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        scalingFactor = ConstantControlParameter.of(0.5);
        recombinationProbability = ConstantControlParameter.of(0.5);
        random = new UniformDistribution();
    }
    
    public ParameterizedDEIndividual(ParameterizedDEIndividual copy) {
        super(copy);
        scalingFactor = copy.scalingFactor;
        recombinationProbability = copy.recombinationProbability;
        random = copy.random;
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public Individual getClone() {
        return new ParameterizedDEIndividual(this);
    }

    /*
     * This method initializes the individual's candidate solution randomly as 
     * well as the two parameters it holds.
     * @param optimizationProblem The problem that is being optimized.
     */
    @Override
    public void initialise(OptimisationProblem problem) {
        Vector candidate = Vector.newBuilder().copyOf(problem.getDomain().getBuiltRepresenation()).buildRandom();
        this.setCandidateSolution(candidate);

        Vector strategy = Vector.copyOf(Sequence.repeat(0.0, candidate.size()));
        this.getProperties().put(EntityType.STRATEGY_PARAMETERS, strategy);
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        
        if(!scalingFactor.wasSetByUser()) {
            scalingFactor.updateParameter(random.getRandomNumber(((BoundedModifiableControlParameter) scalingFactor).getLowerBound(), 
                    ((BoundedModifiableControlParameter) scalingFactor).getUpperBound()));
        }
        
        if(!recombinationProbability.wasSetByUser()) {
            recombinationProbability.updateParameter(random.getRandomNumber(((BoundedModifiableControlParameter) recombinationProbability).getLowerBound(), 
                    ((BoundedModifiableControlParameter) recombinationProbability).getUpperBound()));
        }
        
    }
    
    /**
     * Create a textual representation of the current {@linkplain ParameterizedDEIndividual}. The
     * returned {@linkplain String} will contain both the genotypes and penotypes for
     * the current {@linkplain ParameterizedDEIndividual}.
     * @return The textual representation of this {@linkplain ParameterizedDEIndividual}.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getCandidateSolution().toString());
        str.append(scalingFactor);
        str.append(recombinationProbability);
        return str.toString();
    }
    
    /* 
     * Sets the scaling factor to the value received as a parameter.
     * @param parameter The new parameter value for the scaling factor
     */
    public void setScalingFactor(ParameterAdaptingControlParameter parameter) {
        scalingFactor = (ParameterAdaptingControlParameter) parameter.getClone();
    }
    
    /* 
     * Sets the recombination probability to the value received as a parameter.
     * @param parameter The new parameter value for the recombination probability
     */
    public void setRecombinationProbability(ParameterAdaptingControlParameter parameter) {
        recombinationProbability = (ParameterAdaptingControlParameter) parameter.getClone();
    }
    
    /*
     * Gets the current scaling factor.
     * @return scalingFactor The scaling factor
     */
    public ParameterAdaptingControlParameter getScalingFactor() {
        return scalingFactor;
    }
    
    /*
     * Gets the current recombination probability.
     * @return recombinationProbability The recombination probability
     */
    public ParameterAdaptingControlParameter getRecombinationProbability() {
        return recombinationProbability;
    }
}
