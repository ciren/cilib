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
    
    public ParameterizedWorkerBee() {
        failureCount = 0;
        this.forageLimit = ConstantControlParameter.of(500);
        explorerBeeUpdateLimit = ConstantControlParameter.of(1.0);
        this.parameterPositionUpdateStrategy = new VisualPositionUpdateStategy();
        this.random = new UniformDistribution();
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The isntance to copy.
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
    
    private HoneyBee getParameterEntity(ParameterizedWorkerBee workerBee) {
        WorkerBee bee = new WorkerBee();
        bee.setPosition(Vector.of(workerBee.getForageLimit().getParameter(), workerBee.getExplorerBeeUpdateLimit().getParameter()));
        bee.getProperties().put(EntityType.FITNESS, workerBee.getFitness());
        bee.setDimension(2);
        return bee;
    }
    
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
    
    public void setParameterPositionUpdateStrategy(BeePositionUpdateStrategy strategy) {
        parameterPositionUpdateStrategy = strategy;
    }
    
    public BeePositionUpdateStrategy getParameterPositionUpdateStrategy() {
        return parameterPositionUpdateStrategy;
    }
    
    public void setRandom(ProbabilityDistributionFuction random) {
        this.random = random;
    }
    
    public ProbabilityDistributionFuction getRandom() {
        return random;
    }
    
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
