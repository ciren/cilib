/*
 * GCParticle.java
 *
 * Created on January 19, 2003, 5:05 PM
 *
 * 
 * Copyright (C) 2003 - Edwin S. Peer
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

package net.sourceforge.cilib.PSO;

import java.util.*;
import net.sourceforge.cilib.Random.*;

/**
 *
 * @author  espeer
 */
public class GCParticle extends Particle {
    
    public GCParticle() {
        super();
        successCount = 0;
        failureCount = 0;
        rho = 1;
        fitnessImproved = false;
        previousNeighbourhoodBest = this;
    }
    
    
    public void setFitness(double fitness) {
        previousNeighbourhoodBest = (GCParticle) getNeighbourhoodBest();
        if (fitness > previousNeighbourhoodBest.getBestFitness()) {
            fitnessImproved = true;
        }
        else {
            fitnessImproved = false;
        }
        super.setFitness(fitness);
    }
    
    public void updateRho() {
        if (getNeighbourhoodBest() == this) {
            if (previousNeighbourhoodBest != this) {
                rho = previousNeighbourhoodBest.rho;
                successCount = 0;
                failureCount = 0;
            }
            
            if (fitnessImproved) {
                ++successCount;
                failureCount = 0;
                if (successCount > pso.getSuccessThreshold()) {
                    rho *= pso.getRhoExpandCoefficient();
                    if (rho > pso.getVmax()) {
                        rho = pso.getVmax();
                    }
                }
            }
            else {
                ++failureCount;
                successCount = 0;
                if (failureCount > pso.getFailureThreshold()) {
                    rho *= pso.getRhoContractCoefficient();
                    if (rho < 1e-300) {
                        rho = 1e-300;
                    }
                }
            }
        }
    }
    
    public void updateVelocity() {
        if (getNeighbourhoodBest() == this) {   
            for (int i = 0; i < getDimension(); ++i) {
                getVelocity()[i] = getBestPosition()[i] - getPosition()[i] 
                  + pso.getInertia() * getVelocity()[i] 
                  + rho * (1 - 2 * pso.getRhoRandomGenerator().nextFloat());
            }
        }
        else {
            super.updateVelocity();
        }
    }
    
    public void setPSO(PSO pso) {
        this.pso = (GCPSO) pso;
        super.setPSO(pso);
    }
    
    private double rho;
    private int successCount;
    private int failureCount;
    
    private boolean fitnessImproved;
    private GCParticle previousNeighbourhoodBest;
    private GCPSO pso;
}
