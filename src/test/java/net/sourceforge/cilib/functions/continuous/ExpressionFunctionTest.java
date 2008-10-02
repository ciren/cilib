/**
 * Copyright (C) 2003 - 2008
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

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Before;
import org.junit.Test;
import org.nfunk.jep.JEP;

public class ExpressionFunctionTest {
	
	private JEP jep;
	
	@Before
	public void setup() {
		jep = new JEP();
	}
	
	@Test
	public void compareWithSpherical() {
		jep.addVariable("x", 20);
		jep.parseExpression("x^2");
		
		assertEquals(400.0, jep.getValue(), Double.MIN_NORMAL);
	}
	
	@Test
	public void compareWithRastrigin() {
		jep.addVariable("x", 5);
		jep.addStandardConstants();
		jep.addStandardFunctions();
		
		jep.parseExpression("10 + x^2 - 10*cos(2*pi*x)");
		Rastrigin rastrigin = new Rastrigin();
		rastrigin.setDomain("R(-5.12,5.12)^1");
		Vector vector = new Vector();
		vector.add(new Real(5.0));
		
		assertEquals(rastrigin.evaluate(vector), jep.getValue(), Double.MIN_NORMAL);
	}

}
