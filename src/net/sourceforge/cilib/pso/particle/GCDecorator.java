/*
 * GCDecorator.java
 *
 * Created on September 22, 2003, 2:25 PM
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
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

package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.velocityupdatestrategies.GCVelocityUpdate;



/**
 *
 * @author  Edwin Peer
 */
public class GCDecorator extends ParticleDecorator {
    
    /** Creates a new instance of GCParticleDecorator */
    public GCDecorator(Particle target) {       
        super(target);        
        
        successCount = 0;
        failureCount = 0;
        rho = 1;
        fitnessImproved = false;
        previousNeighbourhoodBest = super.getNeighbourhoodBest();
    }

    public GCDecorator clone() {
        return new GCDecorator(this.getTarget().clone()); //super.clone();
    }
    
    public void setFitness(Fitness fitness) {
        previousNeighbourhoodBest = getNeighbourhoodBest();
        if (fitness.compareTo(previousNeighbourhoodBest.getBestFitness()) > 0) {
            fitnessImproved = true;
        }
        else {
            fitnessImproved = false;
        }
        super.setFitness(fitness);
    }
    
    
    public void updateVelocity() {
    	this.getVelocityUpdateStrategy().updateVelocity(this);
    }
    
    public void updateRho(GCVelocityUpdate vu) {
        if (getNeighbourhoodBest().getId() == getId()) {
            if (previousNeighbourhoodBest.getId() != getId()) {
                rho = GCDecorator.extract(previousNeighbourhoodBest).getRho();
                successCount = 0;
                failureCount = 0;
            }
            
            if (fitnessImproved) {
                ++successCount;
                failureCount = 0;
                if (successCount > vu.getSuccessThreshold()) {
                    rho *= vu.getRhoExpandCoefficient();
                    if (rho > vu.getStandardVelocityUpdate().getVMax().getParameter()) {
                        rho = vu.getStandardVelocityUpdate().getVMax().getParameter();
                    }
                }
            }
            else {
                ++failureCount;
                successCount = 0;
                if (failureCount > vu.getFailureThreshold()) {
                    rho *= vu.getRhoContractCoefficient();
                    if (rho < 1e-300) {
                        rho = 1e-300;
                    }
                }
            }
        }
    }
    
    public double getRho() {
        return rho;
    }
    
    public static GCDecorator extract(Particle particle) {
        return (GCDecorator) particle.getDecorator(GCDecorator.class);
    }
    
    private double rho;
    private int successCount;
    private int failureCount;
    
    private boolean fitnessImproved;
    private Particle previousNeighbourhoodBest;
}
