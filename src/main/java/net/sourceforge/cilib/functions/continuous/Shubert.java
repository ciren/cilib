/*
 * Shubert.java
 *
 * Created on June 4, 2003, 1:46 PM
 *
 * 
 * Copyright (C) 2003 - 2006 
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
 *   
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;


/**
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Clive Naicker
 * @version 1.0
 */

public class Shubert extends ContinuousFunction {

    public Shubert() {
        setDomain("R(-10, 10)^2");
    }
    
    public Object getMinimum() {
        return new Double(-186.7309088);
    }

    public double evaluate(Vector x) {
        double result = 1.0;
        for (int i=0; i < getDimension(); ++i) {
            double result2 = 0.0;
            for (int j=1; j<=5; j++) {
                result2 += j*Math.cos((j+1)*x.getReal(i) + j);
            }
            result *= result2;
        }
        return result;
    }

    /*
    public static void main(String[] args) {
        Shubert t = new Shubert();
        System.out.println(t.evaluate(new double[] {-7.08351, 4.85806}));
        System.out.println(t.evaluate(new double[] {5.48286, 4.85806}));
        System.out.println(t.evaluate(new double[] {4.85806, -7.08351}));
        System.out.println(t.evaluate(new double[] {4.85806, 5.48286}));
    }
    */
}