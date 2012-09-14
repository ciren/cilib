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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * Implementation of Goldberg's order-3 Deceptive problem.
 * Intended for bit strings that are multiples of length 3.
 * <p>
 * References: 
 * </p>
 * <ul><li>
 * Al-kazemi, Buthainah Sabeeh No'man and Mohan, Chilukuri K., "Multi-phase 
 * discrete particle swarm optimization" (2000). Electrical Engineering and 
 * Computer Science. Paper 54.
 * </li></ul>
 */
public class Order3Deceptive implements ContinuousFunction {
        
    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {                
        double result = 0.0;
        
        for (int i = 0; i < input.size()-2; i+=3) {
            result += getValue(input.copyOfRange(i, i+3));
        }
        
        return result;
    }
    
    private Double getValue(Vector input) {
        
        int sum = 0;
        for(int i = 0; i < input.size(); i++) {
            if (input.booleanValueOf(i)) {
                sum++;
            }
        }     
        
        if (sum == 3) {
            return 1.0;
        } else {
            return 0.9 - (0.3 * sum);
            
        }
    }
}
