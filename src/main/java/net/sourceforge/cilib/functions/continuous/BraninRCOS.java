/* 
 * BraninRCOS.java
 *
 * Created on June 4, 2003, 4:57 PM 
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
 */ 

package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library) The Branin Function.
 * Minima located at (-PI, 12.275), (PI, 2.275), (9.42478, 2.475)
 * All Minima are equal to 0.3978837
 * </p>
 * 
 * Characteristics:
 * <ul>
 *  <li>Multimodal</li>
 * </ul>
 * 
 * @author Clive Naicker
 * @version 1.0
 */

public class BraninRCOS extends ContinuousFunction {
	private static final long serialVersionUID = -8231787667614872490L;
	
	private double a = 1.0;
    private double b = 5.1/(4*Math.PI*Math.PI);
    private double c = 5.0/Math.PI;
    private double d = 6.0;
    private double e = 10.0;
    private double f = 1.0/(8.0*Math.PI);

    public BraninRCOS() {

        a = 1.0;
        b = 5.1/(4*Math.PI*Math.PI);
        c = 5.0/Math.PI;
        d = 6.0;
        e = 10.0;
        f = 1.0/(8.0*Math.PI);
        
        //constraint.add(new DimensionValidator(2));
        //constraint.add(new ContentValidator(0, new QuantitativeBoundValidator(new Double(-5), new Double(15))));
        //constraint.add(new ContentValidator(1, new QuantitativeBoundValidator(new Double(0), new Double(15))));
        
        setDomain("R(-5,10),R(0,15)");
    }

    public Object getMinimum() {
        return new Double(0.3978873);
    }
    
    public double evaluate(Vector x) {
        double x1 = x.getReal(0);
        double x2 = x.getReal(1);

        double result = a*Math.pow((x2 - b*x1*x1 + c*x1 - d), 2) + e*(1 - f)*Math.cos(x1) + e;
        return result;
    }

    /*
    public static void main(String[] args) {
        BraninRCOS t = new BraninRCOS();
        System.out.println(t.evaluate(new double[] {-Math.PI, 12.275}));
        System.out.println(t.evaluate(new double[] {Math.PI, 2.275}));
        System.out.println(t.evaluate(new double[] {9.42478, 2.475}));
    }
    */

}