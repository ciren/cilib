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
 * Implementation of Mulhenbein's order-5 problem.
 * Intended for bit strings that are multiples of length 5.
 * <p>
 * References: 
 * </p>
 * <ul><li>
 * Al-kazemi, Buthainah Sabeeh No'man and Mohan, Chilukuri K., "Multi-phase 
 * discrete particle swarm optimization" (2000). Electrical Engineering and 
 * Computer Science. Paper 54.
 * </li></ul>
 */
public class Order5Deceptive implements ContinuousFunction {
        
    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {                
        double result = 0.0;
        
        for (int i = 0; i < input.size()-4; i+=5) {
            Vector.Builder builder = Vector.newBuilder();
            
            for(int j = 0; j < 5; j++) {
                builder.add(input.get(i+j));
            }
            
            result += getValue(builder.build());
        }
        
        return Math.round(result * 10) / 10.0d;
    }
    
    /**
     * Maps strings in the following manner.
     * 00000 = 4.0
     * 00001 = 3.0
     * 00011 = 2.0
     * 00111 = 1.0
     * 11111 = 3.5
     * other = 0.0
     */
    private Double getValue(Vector input) {
        
        int decimalValue = 0;
        for(int i = 0; i < input.size(); i++) {
            if (input.booleanValueOf(i)) {
                decimalValue += Math.pow(2, input.size() - i - 1);
            }
        }    
        
        double value;
        switch (decimalValue) {
            case 0: value = 4.0;
                break;
            case 1: value = 3.0;
                break;
            case 3: value = 2.0;
                break;
            case 7: value = 1.0;
                break;
            case 31: value = 3.5;
                break;
            default: value = 0.0;
                
        }
        
        return value;
    }
}
