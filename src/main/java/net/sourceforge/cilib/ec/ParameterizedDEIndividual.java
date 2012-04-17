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
    
    @Override
    public Individual getClone() {
        return new ParameterizedDEIndividual(this);
    }
/*
    @Override
    public void calculateFitness() {
        this.getProperties().put(EntityType.FITNESS, this.getFitnessCalculator().getFitness(this));
    }
*/
    /**
     * {@inheritDoc}
     */
    /*@Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if ((object == null) || (this.getClass() != object.getClass())) {
            return false;
        }

        ParameterizedDEIndividual other = (ParameterizedDEIndividual) object;
        return super.equals(other);
    }*/
    
    /**
     * {@inheritDoc}
     */
    /*@Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        return hash;
    }*/
    
    /**
     * Resets the fitness to <code>InferiorFitness</code>.
     */
    /*@Override
    public void resetFitness() {
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
    }*/
    
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

    /*@Override
    public int getDimension() {
        return getCandidateSolution().size();
    }

    @Override
    public void reinitialise() {
        throw new UnsupportedOperationException("Not supported yet. Not implemented in individual");
    }*/
    
    /**
     * {@inheritDoc}
     */
    /*@Override
    public void setCandidateSolution(StructuredType type) {
        super.setCandidateSolution(type);
    }

    @Override
    public int compareTo(Entity o) {
        return this.getFitness().compareTo(o.getFitness());
    }*/
    
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
    
    public void setScalingFactor(ParameterAdaptingControlParameter parameter) {
        scalingFactor = (ParameterAdaptingControlParameter) parameter.getClone();
    }
    
    public void setRecombinationProbability(ParameterAdaptingControlParameter parameter) {
        recombinationProbability = (ParameterAdaptingControlParameter) parameter.getClone();
    }
    
    public ParameterAdaptingControlParameter getScalingFactor() {
        return scalingFactor;
    }
    
    public ParameterAdaptingControlParameter getRecombinationProbability() {
        return recombinationProbability;
    }
}
