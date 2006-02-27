/*
 * DissipativeStep.java
 *
 * Created on February 26, 2003, 12:13 PM
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
 * <p>
 * This implements the dissipative PSO (DPSO). Apply this to the standard PSO 
 * using {@link PSO#setDissipativeStep(DissipativeStep)}.
 * </p><p>
 * References: 
 * </p><ul><li>
 * X Xie, W Zang, Z Yang, "A dissipative swarm optimization"'
 * Proceedings of the IEEE Congress on Evolutionary Computing (CEC 2002),
 * Honolulu, Hawaii USA, May 2002
 * </li></ul> 
 *
 * @author  espeer
 */
public class DissipativeStep {
    
    /** Creates a new instance of DissipativeStep */
    public DissipativeStep() {
        randomGenerator = new KnuthSubtractive();
        velocityThreshold = 0.001f;
        positionThreshold = 0.002f;
    }
    
    public void execute(Particle particle) {
        if (randomGenerator.nextFloat() < velocityThreshold) {
            for (int i = 0; i < particle.getDimension(); ++i) {
                particle.getVelocity()[i] = randomGenerator.nextFloat() 
                * (pso.getOptimisationProblem().getDomain(i).getUpperBound()
                   - pso.getOptimisationProblem().getDomain(i).getLowerBound());
            }
        }
        if (randomGenerator.nextFloat() < positionThreshold) {
            for (int i = 0; i < particle.getDimension(); ++i) {
                particle.getPosition()[i] = randomGenerator.nextDouble() 
                * (pso.getOptimisationProblem().getDomain(i).getUpperBound()
                   - pso.getOptimisationProblem().getDomain(i).getLowerBound())
                + pso.getOptimisationProblem().getDomain(i).getLowerBound();
            }
        }
    }
    
    public void setRandomGenerator(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }
    
    public Random getRandomGenerator() {
        return randomGenerator;
    }
    
    public void setVelocityThreshold(float velocityThreshold) {
        this.velocityThreshold = velocityThreshold;
    }
    
    public void setPositionThreshold(float positionThreshold) {
        this.positionThreshold = positionThreshold;
    }
    
    protected void setPSO(PSO pso) {
        this.pso = pso;
        randomGenerator.setSeed(pso.getSeedRandomGenerator().nextLong());
    }
    
    private PSO pso;
    
    private Random randomGenerator;
    private float velocityThreshold;
    private float positionThreshold;
}
