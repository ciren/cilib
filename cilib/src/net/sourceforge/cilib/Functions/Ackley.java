/*
 * Ackley.java
 *
 * Created on January 11, 2003, 2:09 PM
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

package net.sourceforge.cilib.Functions;

import net.sourceforge.cilib.Problem.*;
import java.lang.*;

/**
 *
 * @author  espeer
 */
public class Ackley extends Function {
    
    public static final int DEFAULT_DIMENSION = 30;
    public static final Domain DEFAULT_DOMAIN = new Domain(-30, 30);
    
    public Ackley() {
        super(DEFAULT_DIMENSION, DEFAULT_DOMAIN, 0);
    }
    
    public double evaluate(double[] x) {
        double sumsq = 0.0;
        double sumcos = 0.0;
        for (int i = 0; i < dimension; ++i) {
            sumsq += x[i] * x[i];
            sumcos += Math.cos(2 * Math.PI * x[i]);
        }
        return - 20.0 * Math.exp(-0.2 * Math.sqrt(sumsq / dimension)) 
               - Math.exp(sumcos / dimension) + 20 + Math.E;
    }
}

