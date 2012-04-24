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
package net.sourceforge.cilib.boa.bee;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.boa.ABC;
import net.sourceforge.cilib.boa.positionupdatestrategies.BeePositionUpdateStrategy;
import net.sourceforge.cilib.boa.positionupdatestrategies.VisualPositionUpdateStategy;
import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ParameterAdaptingControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class ParameterizedWorkerBee extends WorkerBee{
    private BeePositionUpdateStrategy parameterPositionUpdateStrategy;
    private ProbabilityDistributionFuction random;
    
    /*
     * Default constructor. Initializes variables.
     */
    public ParameterizedWorkerBee() {
        failureCount = 0;
        this.forageLimit = ConstantControlParameter.of(500);
        explorerBeeUpdateLimit = ConstantControlParameter.of(1.0);
        this.parameterPositionUpdateStrategy = new VisualPositionUpdateStategy();
        this.random = new UniformDistribution();
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public ParameterizedWorkerBee(ParameterizedWorkerBee copy) {
        super(copy);
        this.failureCount = copy.failureCount;
        this.forageLimit = copy.forageLimit.getClone();
        this.explorerBeeUpdateLimit = copy.explorerBeeUpdateLimit;
        this.parameterPositionUpdateStrategy = copy.parameterPositionUpdateStrategy;
        this.random = copy.random;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterizedWorkerBee getClone() {
        return new ParameterizedWorkerBee(this);
    }
    
    /*
     * {@inheritDoc}
     * This method does not only update the position, but also updates the parameter values
     * held by the bee.
     */
    @Override
    public void updatePosition() {
        ABC algorithm = (ABC) AbstractAlgorithm.get();
        HoneyBee target = targetSelectionStrategy.on(algorithm.getWorkerBees()).select();
        HoneyBee targetParameter = getParameterEntity((ParameterizedWorkerBee) target);
        
        HoneyBee thisParameter = getParameterEntity(this);
        
        boolean success = this.positionUpdateStrategy.updatePosition(this, target);
        this.parameterPositionUpdateStrategy.updatePosition(thisParameter, targetParameter);
        
        forageLimit = getControlParameter(thisParameter.getPosition(), 0);
        explorerBeeUpdateLimit = getControlParameter(thisParameter.getPosition(), 1);
        
        if (!success) {
            failureCount++;
            if (failureCount >= forageLimit.getParameter()) {
                failureCount = 0;
                ExplorerBee explorerBee = algorithm.getExplorerBee();
                if (explorerBee.searchAllowed(algorithm.getIterations(), explorerBeeUpdateLimit)) {
                    this.setPosition(explorerBee.getNewPosition(algorithm.getIterations(), this.getPosition()));
                    explorerBee.setNumberOfUpdates(explorerBee.getNumberOfUpdates() - 1);
                    forageLimit = getControlParameter(explorerBee.getNewPosition(algorithm.getIterations(), thisParameter.getPosition()), 0);
                    explorerBeeUpdateLimit = getControlParameter(explorerBee.getNewPosition(algorithm.getIterations(), thisParameter.getPosition()), 1);
                }
            }
        }
        
    }
    
    /*
     * Returns a HoneyBee holding the parameters as its position
     * @param workerBee The bee olding the parameters separately
     * @return The bee whose position consists of the parameter values.
     */
    private HoneyBee getParameterEntity(ParameterizedWorkerBee workerBee) {
        WorkerBee bee = new WorkerBee();
        bee.setPosition(Vector.of(workerBee.getForageLimit().getParameter(), workerBee.getExplorerBeeUpdateLimit().getParameter()));
        bee.getProperties().put(EntityType.FITNESS, workerBee.getFitness());
        bee.setDimension(2);
        return bee;
    }
    
    /*
     * Returns the control parameter at the index given, which is held by 
     * the vector provided.
     * @param parameterPosition The vector holding the parameter values
     * @param index The index of the aprameter required
     * @return The control parameter which must be assigned to the parameter 
     * requestiong thios value
     */
    private ControlParameter getControlParameter(Vector parameterPosition, int index) {
        ControlParameter newControlParameter;
        
        if(index == 0) {
            if(this.getForageLimit() instanceof ConstantControlParameter)
                return ConstantControlParameter.of(parameterPosition.get(index).doubleValue()); 
        } else if (index == 1) {
            if(this.getExplorerBeeUpdateLimit() instanceof ConstantControlParameter)
                return ConstantControlParameter.of(parameterPosition.get(index).doubleValue());
        }
        
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(parameterPosition.get(index).doubleValue());
        return parameter;
    }
    
    /*
     * Set the position update strategy that must be used for the parameters
     * @param strategy The strategy to be used
     */
    public void setParameterPositionUpdateStrategy(BeePositionUpdateStrategy strategy) {
        parameterPositionUpdateStrategy = strategy;
    }
    
    /*
     * Returns the position update strategy being used for updating the parameters
     * @return The position update strategy used to update the parameters
     */
    public BeePositionUpdateStrategy getParameterPositionUpdateStrategy() {
        return parameterPositionUpdateStrategy;
    }
    
    /*
     * Set the probability distribution function to be used to initialize 
     * non-initialized parameters.
     * @param random The probability distribution function
     */
    public void setRandom(ProbabilityDistributionFuction random) {
        this.random = random;
    }
    
    /*
     * Returns the random generator used to initialize parameters
     * @return The random generator
     */
    public ProbabilityDistributionFuction getRandom() {
        return random;
    }
    
    /*
     * Sets the forage limit to the one provided if the one provided was set by the user.
     * Otherwise, it randomly initializes it.
     * @param forageLimit The forage limit provided by the ABC class
     */
    @Override
    public void setForageLimit(ControlParameter forageLimit){
        if(((ParameterAdaptingControlParameter) forageLimit).wasSetByUser()) {
            this.forageLimit = forageLimit;
        } else {
            BoundedModifiableControlParameter theForageLimit = (BoundedModifiableControlParameter) forageLimit;
            theForageLimit.updateParameter(random.getRandomNumber(theForageLimit.getLowerBound(), theForageLimit.getUpperBound()));
            this.forageLimit = theForageLimit;
        }
    }
    
    /*
     * Sets the explorer bee update limit to the one provided if the one provided was set by the user.
     * Otherwise, it randomly initializes it.
     * @param explorerBeeUpdateLimit The explorer bee update limit provided by the ABC class
     */
    @Override
    public void setExplorerBeeUpdateLimit(ControlParameter explorerBeeUpdateLimit){
        if(((ParameterAdaptingControlParameter) explorerBeeUpdateLimit).wasSetByUser()) {
            this.explorerBeeUpdateLimit = explorerBeeUpdateLimit;
        } else {
            BoundedModifiableControlParameter theExplorerBeeUpdateLimit = (BoundedModifiableControlParameter) explorerBeeUpdateLimit;
            theExplorerBeeUpdateLimit.updateParameter(random.getRandomNumber(theExplorerBeeUpdateLimit.getLowerBound(), theExplorerBeeUpdateLimit.getUpperBound()));
            this.explorerBeeUpdateLimit = theExplorerBeeUpdateLimit;
        }
    }
}
