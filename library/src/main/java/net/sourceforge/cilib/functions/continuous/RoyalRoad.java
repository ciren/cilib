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

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * Implementation of Hollands Royal Road function.
 * Intended for bit strings of length 240
 * <p>
 * References: 
 * </p>
 * <ul><li>
 * Terry Jones;, "A Description of Holland's Royal Road Function" (1994). 
 * Sante Fe Institute
 * </li></ul>
 */
public class RoyalRoad implements ContinuousFunction {
    
    private Integer k;
    private Integer b;
    private Integer g;
    private Integer mstar;    
    private Double v;
    private Double ustar;
    private Double u;
    
    public RoyalRoad() {
        // set up default parameters as described by Holland
        k = 4;
        b = 8;
        g = 7;
        mstar = 4;
        v = 0.02;
        ustar = 1.0;
        u = 0.3;
    }
        
    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        if (input.size() != 240) {
            return 0.0d;
        }
        
        return part(input) + bonus(input);
    }
    
    private Double part(Vector input) {
        double partFitness = 0.0;
        
        List<Vector> blocks = getBlocks(input);
        for (Vector block : blocks) {
            int numOnes = 0;
            for(int i = 0; i < block.size(); i++) {
                if (block.booleanValueOf(i)) {
                    numOnes++;
                }
            }
            
            if (numOnes == b) {
                // block is complete
                partFitness += 0.0;
            } else {
                if (numOnes <= mstar) {
                    partFitness += numOnes * v;
                } else {
                    partFitness += (numOnes - mstar) * (-v);
                }
            }
        }
    
        return partFitness;
    }
    
    private Double bonus(Vector input) {
        int levels = k+1;
        double bonusFitness = 0;
        
        List<Vector> blocks = getBlocks(input);
        
        for(int level = 0; level < levels; level++) {
            boolean first = true;
            
            for(int j = 0; j < blocks.size(); j+= Math.pow(2,level)) {
                boolean contiguous = true;
                
                for(int k = 0; k < Math.pow(2,level); k++) {
                    if (!isComplete(blocks.get(j+k))) {
                        contiguous = false;
                    }
                }
                
                if (contiguous) {
                    if (first) {
                        bonusFitness += ustar;
                        first = false;
                    } else {
                        bonusFitness += u;
                    }
                }
            }
        }
        
        return bonusFitness;
    }
    
    private List<Vector> getBlocks(Vector input) {
        Integer region = b + g;
        List<Vector> blocks = new ArrayList<Vector>();
        
        for(int i = 0; i < input.size(); i += region) {
            Vector.Builder builder = Vector.newBuilder();
            for(int j = 0; j < b; j++) {
                builder.add(input.booleanValueOf(i+j));
            }
            blocks.add(builder.build());
        }
        return blocks;
    }
    
    private boolean isComplete(Vector input) {
        for(int i = 0; i < input.size(); i++) {
            if (!input.booleanValueOf(i)) {
                return false;
            }
        }
        return true;
    }
}
