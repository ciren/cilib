/*
 * Salomon.java
 * 
 * Created on July 07, 2007
 *
 * Copyright (C) 2003 - 2007
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
import net.sourceforge.cilib.type.types.Vector;

/**
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Nonseperable</li>
 * </ul>
 * f(x) = 0; x = (0,0,...,0); x_i e (-600,600)
 * @author Olusegun Olorunda
 */
public class Salomon extends ContinuousFunction {
	private static final long serialVersionUID = -6002240316648057218L;

	public Salomon() {
		setDomain("R(-600, 600)^30");
	}

	public Object getMinimum() {
		return new Double(0);
	}

	@Override
	public double evaluate(Vector x) {
		double functionValue = 0.0;
		double sumSquares = 0.0;

		for (int i = 0; i < x.getDimension(); i++) {
			sumSquares += x.getReal(i) * x.getReal(i);
		}

		functionValue = -(Math.cos(2 * Math.PI * Math.sqrt(sumSquares))) + (0.1 * Math.sqrt(sumSquares)) + 1;

		return functionValue;
	}
}
