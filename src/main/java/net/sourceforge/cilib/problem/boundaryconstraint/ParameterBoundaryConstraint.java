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
package net.sourceforge.cilib.problem.boundaryconstraint;

import java.util.ArrayList;
import javax.lang.model.element.Parameterizable;
import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ParameterAdaptingPSOControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Enforce predefined boundary constraints on {@link Entity} instances that are
 * operating in the current search space. Various strategies are available to
 * enforce these boundary constraints on the provided {@link Entity} objects.
 * 
 * @author Kristina
 */
public class ParameterBoundaryConstraint implements BoundaryConstraint{
    BoundaryConstraint constraint;
    
    public ParameterBoundaryConstraint() {
        constraint = new UnconstrainedBoundary();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BoundaryConstraint getClone() {
        return this;
    }
    
    
    /*
     * Create an entity representation of the particle's parameters and send this 
     * representation to have boundary constraints enforced.
     * 
     * PS: it is ugly, but it's the only way it will work with the current architecture.
     * 
     * @param entity The particle whose parameters are being constrained
     */
    @Override
    public Entity enforce(Entity entity) {
        ParameterizedParticle receivedEntity = (ParameterizedParticle) entity.getClone();
        ParameterizedParticle parameterEntity = new ParameterizedParticle();
        
        Bounds bounds;
        BoundedModifiableControlParameter parameter;
        Real value;
        Vector.Builder parameterSolution = Vector.newBuilder();
        Vector.Builder bestParameterSolution = Vector.newBuilder();
        Vector.Builder velocityParameter = Vector.newBuilder();
        ArrayList<ControlParameter> parameterList = new ArrayList<ControlParameter>();
        
        parameterList.add(receivedEntity.getInertia());
        parameterList.add(receivedEntity.getSocialAcceleration());
        parameterList.add(receivedEntity.getCognitiveAcceleration());
        parameterList.add(receivedEntity.getVmax());
        
        // Set the parameters and bounds of the candidate solution. Set the best 
        // position as well as the velocity
        for(ControlParameter param : parameterList) {
            if(param instanceof BoundedModifiableControlParameter){
                parameter = (BoundedModifiableControlParameter) param.getClone();
                bounds = new Bounds(parameter.getLowerBound(), parameter.getUpperBound());
                value =  Real.valueOf(parameter.getParameter(), bounds);
            } else {
                parameter = new BoundedModifiableControlParameter();
                parameter.setParameter(param.getParameter());
                bounds = new Bounds(0, 1);
                value =  Real.valueOf(parameter.getParameter(), bounds);
            }
            parameterSolution.add(value);
            bestParameterSolution.add(parameter.getBestValue().getParameter());
            velocityParameter.add(parameter.getVelocity());
            
       }
        
        //Create an entity built from the four parameters
        parameterEntity.setCandidateSolution(parameterSolution.build());
        parameterEntity.getProperties().put(EntityType.Particle.VELOCITY, velocityParameter.build());
        parameterEntity.getProperties().put(EntityType.Particle.BEST_POSITION, bestParameterSolution.build());
        
        //enforce boundary constraints
        ParameterizedParticle resultingEntity = (ParameterizedParticle) constraint.enforce(parameterEntity);
        ParameterizedParticle newEntity = (ParameterizedParticle) constraint.enforce(receivedEntity);
        
        receivedEntity = newEntity.getClone();
        
        Vector candidateSolution = (Vector) resultingEntity.getCandidateSolution();
        Vector bestSolution = (Vector) resultingEntity.getBestPosition();
        Vector velocity = (Vector) resultingEntity.getVelocity();
        
        //set the resulting values of the entity to be returned,
        if(receivedEntity.getInertia() instanceof BoundedModifiableControlParameter) {
           receivedEntity.getInertia().setParameter((candidateSolution.get(0).doubleValue()));
           receivedEntity.getInertia().setBestValue(bestSolution.get(0).doubleValue());
           receivedEntity.getInertia().setVelocity(velocity.get(0).doubleValue());
        }
        if(receivedEntity.getSocialAcceleration() instanceof BoundedModifiableControlParameter) {
           receivedEntity.getSocialAcceleration().setParameter((candidateSolution.get(1).doubleValue()));
           receivedEntity.getSocialAcceleration().setBestValue(bestSolution.get(1).doubleValue());
           receivedEntity.getSocialAcceleration().setVelocity(velocity.get(1).doubleValue());
        }
        if(receivedEntity.getCognitiveAcceleration() instanceof BoundedModifiableControlParameter) {
           receivedEntity.getCognitiveAcceleration().setParameter((candidateSolution.get(2).doubleValue()));
           receivedEntity.getCognitiveAcceleration().setBestValue(bestSolution.get(2).doubleValue());
           receivedEntity.getCognitiveAcceleration().setVelocity(velocity.get(2).doubleValue());
        }
        if(receivedEntity.getVmax() instanceof BoundedModifiableControlParameter) {
           receivedEntity.getVmax().setParameter((candidateSolution.get(3).doubleValue()));
           receivedEntity.getVmax().setBestValue(bestSolution.get(3).doubleValue());
           receivedEntity.getVmax().setVelocity(velocity.get(3).doubleValue());
        }
        
        
        return receivedEntity;
    }
    
    /*
     * Set the boundary constraint type to be used for the parameters
     * 
     * @param boundaryConstraint The Boundary constraint type 
     */
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        constraint = boundaryConstraint.getClone();
    }
    
    /*
     * Get the boundary constraint type to be used for the parameters
     * 
     * @return boundaryConstraint The Boundary constraint type 
     */
    public BoundaryConstraint getBoundaryConstraint() {
        return constraint;
    }
}
