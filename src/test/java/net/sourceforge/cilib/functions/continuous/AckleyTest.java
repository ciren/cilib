/*
 * AckleyTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 3:17 PM
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

import org.junit.Test;
import static org.junit.Assert.*;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.Ackley;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 *
 * @author Edwin Peer
 */
public class AckleyTest {
    
    public AckleyTest() {
        
    }
    
    /** Test of evaluate method, of class za.ac.up.cs.ailib.Functions.Ackley. */
    @Test
    public void testEvaluate() {
        ContinuousFunction function = new Ackley();
        function.setDomain("R(-30, 30)^3");
        
        Vector x = new MixedVector();
        x.append(new Real(1.0));
        x.append(new Real(2.0));
        x.append(new Real(3.0));
        assertEquals(7.0164536, function.evaluate(x), 0.000000009);
    } 
    
}
