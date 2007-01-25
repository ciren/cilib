/*
 * testSigmoidValues.java
 * 
 * Created on Apr 18, 2005
 *
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.neuralnetwork.testarea;

import net.sourceforge.cilib.neuralnetwork.generic.neuron.SigmoidOutputFunction;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class testSigmoidValues {

	public static void main(String[] args) {
		
		SigmoidOutputFunction sigmoid = new SigmoidOutputFunction();
		
		Double answer = (Double)sigmoid.computeFunction(new Double(1.906));
		Double derivative = (Double)sigmoid.computeDerivativeUsingLastOutput(new Double(0.2588009637));
		
		System.out.println("sigmoid of 1.906 = " + answer);
		System.out.println("Derivative of 0.2588009637 = " + derivative);
		
		Double[] d = new Double[3];
		d[0] = new Double(1);
		d[1] = new Double(2);
		d[2] = new Double(3);
		
		Double[] xxx = d;
		
		System.out.println("D is :");
		for (int i = 0; i < 3; i++){
			System.out.print("   " + d[i]);
		}
		
		System.out.println("\nxxx is ");
		for (int i = 0; i < 3; i++){
			System.out.print("   " + xxx[i]);
		}
		
		xxx[1] = new Double(999);
		System.out.println("\nafter xxx change D is :");
		for (int i = 0; i < 3; i++){
			System.out.print("   " + d[i]);
		}
		
		
	}
}
