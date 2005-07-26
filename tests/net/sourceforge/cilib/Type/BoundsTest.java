/*
 * BoundsTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 4:45 PM
 *
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
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

package net.sourceforge.cilib.Type;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author espeer
 */
public class BoundsTest extends TestCase {
    
    public BoundsTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(BoundsTest.class);
        
        return suite;
    }
    
    public void setUp() {
    	
//    	target = new Target();
    }
       
    /*
    public void testClone() {
    	try {
    		Bounds tmp = new Bounds(new Int(1), null, null);
    		
    		Bounds clone = (Bounds) tmp.clone();
    		assertNotSame(tmp, clone);
    		assertEquals(tmp, clone);
    		
    		tmp.setInt(-1);
    		
    		assertFalse(tmp.equals(clone));
    		
    	}
    	catch (CloneNotSupportedException ex) {
    		fail("Failed to clone Bounds");
    	}
    }
    
    public void testEquals() {
    	Bounds a = new Bounds(new Int(1), null, null);
    	Bounds b = new Bounds(new Int(1), null, null);
    	Bounds diff = new Bounds(new Int(-1), null, null);
    	
    	assertTrue(a.equals(a));
    	
    	assertTrue(a.equals(b));
    	assertTrue(b.equals(a));
    	
    	assertFalse(a.equals(diff));
    	assertFalse(diff.equals(a));
    	
    	assertFalse(a.equals(null));
    	
    	Bounds tmp = new Bounds(target, null, null);
    	assertFalse(tmp.equals(diff));
    	assertEquals("[equals(-1)]", target.getMethodString());
    }
    
    public void testHashCode() {
    	Random r = new Random();
    	Int wrapped = new Int(r.nextInt());
    	
    	Bounds tmp = new Bounds(wrapped, null, null);
    	assertEquals(wrapped.hashCode(), tmp.hashCode());
    }
    
    public void testConstructor() {
    	Int one = new Int(1);
    	Int two = new Int(2);
    	Int three = new Int(3);
    	
    	Bounds tmp = new Bounds(one, two, three);
    	
    	assertSame(one, tmp.getTarget());
    	assertSame(two, tmp.getLowerBound());
    	assertSame(three, tmp.getUpperBound());
    }

    public void testGetBit() {
    	boolean bit = new Random().nextBoolean();
    	Bit target = new Bit(bit);
    	Bounds tmp = new Bounds(target, null, null);
    	if (bit) {
    		assertTrue(tmp.getBit());
    	}
    	else {
    		assertFalse(tmp.getBit());
    	}
    }
    
    public void testSetBit() {
    	Bounds tmp = new Bounds(target, null, null);
    	tmp.setBit(true);
    	assertEquals("[setBit(true)]", target.getMethodString());
    }
    
    public void testGetInt() {
    	int value = new Random().nextInt();
    	Int target = new Int(value);
    	
    	Bounds tmp = new Bounds(target, null, null);
    	assertEquals(value, tmp.getInt());
    }
    
    public void testSetInt() {
    	Bounds tmp = new Bounds(target, null, null);
    	tmp.setInt(42);
    	assertEquals("[setInt(42)]", target.getMethodString());
    }
    
    public void testGetReal() {
    	double value = new Random().nextDouble();
    	Real target = new Real(value);
    	
    	Bounds tmp = new Bounds(target, null, null);
    	assertEquals(value, tmp.getReal(), 0);
    }
    
    public void testSetReal() {
    	Bounds tmp = new Bounds(target, null, null);
    	tmp.setReal(1.01);
    	assertEquals("[setReal(1.01)]", target.getMethodString());
    }
    
    public void testGetDomain() {
    	Bounds tmp = new Bounds(target, null, null);
    	assertEquals("Target(,)", tmp.getDomain());
    	assertEquals("[getDomain()]", target.getMethodString());
    	tmp = new Bounds(target, new Int(-1), null);
    	assertEquals("Target(-1,)", tmp.getDomain());
    	tmp = new Bounds(target, null, new Int(1));
    	assertEquals("Target(,1)", tmp.getDomain());
    	tmp = new Bounds(target, new Int(-1), new Int(1));
    	assertEquals("Target(-1,1)", tmp.getDomain());
    }
    
    
    public void testRandomise() {
    	Bounds tmp = new Bounds(target, new Int(-1), new Int(1));
    	tmp.randomise(new Random());
    	assertEquals("[randomise(randomiser, -1, 1)]", target.getMethodString());
    }
    
    public void testToString() {
    	Bounds tmp = new Bounds(target, null, null);
    	assertEquals("Target", tmp.toString());
    	assertEquals("[toString()]", target.getMethodString());
    }
    
    public void testSatisfiesDomain() {
    	Bounds tmp = new Bounds(target, null, null);
    	assertTrue(tmp.satisfiesDomain());

    	tmp = new Bounds(new Int(0), new Int(-1), new Int(1));
    	assertTrue(tmp.satisfiesDomain());
    	
    	tmp = new Bounds(new Int(-1), new Int(-1), new Int(1));
    	assertTrue(tmp.satisfiesDomain());

       	tmp = new Bounds(new Int(1), new Int(-1), new Int(1));
    	assertTrue(tmp.satisfiesDomain());

       	tmp = new Bounds(new Int(1), new Int(-1), new Int(0));
    	assertFalse(tmp.satisfiesDomain());

       	tmp = new Bounds(new Int(-1), new Int(0), new Int(1));
    	assertFalse(tmp.satisfiesDomain());

    }
    

    public void testCompareTo() {
    	int value = new Random().nextInt();
    	Bounds tmp = new Bounds(target, null, null);
    	assertEquals(0, tmp.compareTo(new Int(value)));
    	assertEquals("[compareTo(" + String.valueOf(value) + ")]", target.getMethodString());
    }
  
    private Target target;
    
    private class Target extends Numeric {
    	
    	public Target() {
    		methods = new ArrayList<String>();
    	}

		public boolean equals(Object other) {
			methods.add("equals(" + other.toString() + ")");
			return false;
		}

		public int hashCode() {
			methods.add("hashCode()");
			return 0;
		}

		public boolean getBit() {
			methods.add("getBit()");
			return false;
		}

		public void setBit(boolean value) {
			if (value) {
				methods.add("setBit(true)");
			}
			else {
				methods.add("setBit(false)");
			}
		}

		public int getInt() {
			methods.add("getInt()");
			return 0;
		}

		public void setInt(int value) {
			methods.add("setInt(" + String.valueOf(value) + ")");
		}

		public double getReal() {
			methods.add("getReal()");
			return 0.0;
		}

		public void setReal(double value) {
			methods.add("setReal(" + String.valueOf(value) + ")");
		}

		public Numeric getUpperBound() {
			methods.add("getUpperBound()");
			return null;
		}

		public Numeric getLowerBound() {
			methods.add("getLowerBound()");
			return null;
		}

		public void randomise(Random randomiser) {
			methods.add("randomise(randomiser)");
		}

		protected void randomise(Random randomiser, Numeric bounds) {
			methods.add("randomise(randomiser, " + bounds.getLowerBound() + ", " + bounds.getUpperBound() + ")");
		}

		public String getDomain() {
			methods.add("getDomain()");
			return "Target";
		}
		
		public boolean satisfiesDomain() {
			methods.add("satisfiesDomain()");			
			return false;
		}

		public int compareTo(Numeric other) {
			methods.add("compareTo(" + other.toString() + ")");
			return 0;
		}
		
		public String getMethodString() {
			return methods.toString();
		}
		
		public String toString() {
			methods.add("toString()");
			return "Target";
		}
    	
    	
		private ArrayList<String> methods;
    }
    */
 }
