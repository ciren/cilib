/*
 * DomainVisitorTest.java
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

package net.sourceforge.cilib.Domain;

import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author espeer
 */
public class DomainVisitorTest extends TestCase {
    
    public DomainVisitorTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(DomainVisitorTest.class);
        
        return suite;
    }
    
    public void setUp() {
    }
    
    public void testSingleBit() {
    	DomainComponent domain = factory.newComponent("B");
    	
    	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitBit(Byte[] instances) {
    			assertEquals(1, instances.length);
    			assertEquals(42, instances[0].byteValue());
    		}
    	}, new Object[] { new Byte((byte) 42) });
    }
    
    public void testSingleInt() {
    	DomainComponent domain = factory.newComponent("Z");
 
       	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitInteger(Integer[] instances) {
    			assertEquals(1, instances.length);
    			assertEquals(42, instances[0].intValue());
    		}
    	}, new Object[] { new Integer(42) });
 
    }

    public void testSingleReal() {
    	DomainComponent domain = factory.newComponent("R");
 
       	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitReal(Double[] instances) {
    			assertEquals(1, instances.length);
    			assertTrue(42 ==  instances[0].doubleValue());
    		}
    	}, new Object[] { new Double(42) });
 
    }

    public void testSingleText() {
    	DomainComponent domain = factory.newComponent("T");
    	 
    	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitText(String[] instances) {
    			assertEquals(1, instances.length);
    			assertEquals("abc", instances[0]);
    		}
    	}, new Object[] { "abc" });
    	
    }
 
    public void testSingleSet() {
    	DomainComponent domain = factory.newComponent("S{a,b,c}");
    	 
    	final Object theSet = new HashSet();
    	
    	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitSet(java.util.Set[] instances) {
    			assertEquals(1, instances.length);
    			assertTrue(theSet == instances[0]);
    		}
    	}, new Object[] { theSet });
    
    }
 
    public void testSingleMeasurement() {
    	DomainComponent domain = factory.newComponent("M(net.sourceforge.cilib.Measurement.Time)");
    	
    	net.sourceforge.cilib.Measurement.Measurement time = new net.sourceforge.cilib.Measurement.Time();
    	
     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitInteger(Integer[] instances) {
    			assertEquals(1, instances.length);
    			assertTrue(true);
    		}
    	}, new Object[] { time.getValue() });
   	
    }
    
    public void testSingleUnknown() {
    	DomainComponent domain = factory.newComponent("?");
    	
    	final Object tmp = new Object();
    	
     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitUnknown(Object[] instances) {
    			assertEquals(1, instances.length);
    			assertTrue(tmp == instances[0]);
    		}
    	}, new Object[] { tmp });
    	
    }
    
    public void testRealCompound() {
    	testRealVector(factory.newComponent("R^3"));
    }
    
    public void testRealComposite() {
    	testRealVector(factory.newComponent("[R, R, R]"));
    }
    
    public void testRealVector(DomainComponent domain) {;
    	
    	final double[] one = {1, 2, 3};
    	final double[] two = {0, 0, 0};

     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitRealArray(double[][] instances) {
    			assertEquals(1, instances.length);
    			assertEquals(3, instances[0].length);
    			for (int i = 0; i < 3; ++i) {
    				assertTrue(one[i] == instances[0][i]);
    			}
    		}
    	}, new Object[] { one });

     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitRealArray(double[][] instances) {
    			assertEquals(2, instances.length);
    			assertEquals(3, instances[0].length);
    			assertEquals(3, instances[1].length);

    			for (int i = 0; i < 3; ++i) {
    				assertTrue(one[i] == instances[0][i]);
    			}
    			for (int i = 0; i < 3; ++i) {
    				assertTrue(two[i] == instances[1][i]);
    			}

    		}
    	}, new Object[] { one, two });

    }

    public void testIntVector() {
    	DomainComponent domain = factory.newComponent("Z^3");
    	
    	final int[] one = {1, 2, 3};
    	final int[] two = {0, 0, 0};

     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitIntegerArray(int[][] instances) {
    			assertEquals(1, instances.length);
    			assertEquals(3, instances[0].length);
    			for (int i = 0; i < 3; ++i) {
    				assertTrue(one[i] == instances[0][i]);
    			}
    		}
    	}, new Object[] { one });

     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitIntegerArray(int[][] instances) {
    			assertEquals(2, instances.length);
    			assertEquals(3, instances[0].length);
    			assertEquals(3, instances[1].length);

    			for (int i = 0; i < 3; ++i) {
    				assertTrue(one[i] == instances[0][i]);
    			}
    			for (int i = 0; i < 3; ++i) {
    				assertTrue(two[i] == instances[1][i]);
    			}

    		}
    	}, new Object[] { one, two });

    }

    public void testBitVector() {
    	DomainComponent domain = factory.newComponent("B^3");
    	
    	final BitSet one = new BitSet();
    	final BitSet two = new BitSet();

     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitBitSet(BitSet[] instances) {
    			assertEquals(1, instances.length);
    			assertTrue(one == instances[0]);
    		}
    	}, new Object[] { one });

     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitBitSet(BitSet[] instances) {
    			assertEquals(2, instances.length);
    			assertTrue(one == instances[0]);
    			assertTrue(two == instances[1]);
    		}
    	}, new Object[] { one, two });

    }

    public void testStringVector() {
    	DomainComponent domain = factory.newComponent("T^3");
    	
    	final String[] one = new String[] { "foo", "bar", "baz" };
    	final String[] two = new String[] { "aa", "bb", "cc" };

     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitText(String[] instances) {
    			assertEquals(1, instances.length);
    			assertTrue(count < 3);
    			assertTrue(one[count++].equals(instances[0]));
    		}
    		
    		private int count = 0;
    	}, new Object[] { one });

     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitText(String[] instances) {
    			assertEquals(2, instances.length);
    			assertTrue(count < 3);
    			assertTrue(one[count].equals(instances[0]));
    			assertTrue(two[count++].equals(instances[1]));
    		}
    		
    		private int count = 0;
    	}, new Object[] { one, two });

    }
    
    public void testUnknownVector() {
    	DomainComponent domain = factory.newComponent("?^3");
    	
    	final Class[] one = new Class[] { Integer.class, Double.class, Byte.class };
    	final Integer[] two = new Integer[] { new Integer(0), new Integer(1), new Integer(2) };

     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitUnknown(Object[] instances) {
    			assertEquals(1, instances.length);
    			assertTrue(count < 3);
    			assertTrue(one[count++].equals(instances[0]));
    		}
    		
    		private int count = 0;
    	}, new Object[] { one });

     	domain.acceptDomainVisitor(new TestAdapter() {
    		public void visitUnknown(Object[] instances) {
    			assertEquals(2, instances.length);
       			assertTrue(count < 3);
    			assertTrue(one[count].equals(instances[0]));
    			assertTrue(two[count++].equals(instances[1]));
    		}
    		
    		private int count = 0;
    	}, new Object[] { one, two });

    }

    public void testSimpleMixedVector() {
    	DomainComponent domain = factory.newComponent("[R^3, Z^2]");
    	
    	final Object[] one = new Object[] { new double[] { 0.5, 1.5, 2.5 }, new int[] { -1, 1 } };
    	final Object[] two = new Object[] { new double[] { 10, 20, 30 }, new int[] { -100, 100 } };

    	DomainVisitor visitor = new TestAdapter() {
    		public void visitRealArray(double[][] instances) {
    			assertEquals(1, instances.length);
    			assertEquals(3, instances[0].length);
    			assertEquals(0, count);
    			for (int i = 0; i < 3; ++i) {
    				assertTrue( ((double[]) one[0])[i] == instances[0][i]);
    			}
    			++count;
    		}
    		
    		public void visitIntegerArray(int[][] instances) {
    			assertEquals(1, instances.length);
    			assertEquals(2, instances[0].length);
    			assertEquals(1, count);
    			for (int i = 0; i < 2; ++i) {
    				assertTrue( ((int[]) one[1])[i] == instances[0][i]);
    			}
    			++count;
    		}
    		
    		/*public void checkMethodCalls() {
    			assertEquals(2, count);
    		}*/
    		
    		private int count = 0;
    		
    	};
    	
     	domain.acceptDomainVisitor(visitor, new Object[] { one });
     	try {
     		visitor.getClass().getMethod("checkMethodCalls", (Class []) null).invoke(visitor, (Object[]) null);
     	}
     	catch (InvocationTargetException ex) {
     		assertTrue(false);
     	}
     	catch (IllegalAccessException ex) {
     		assertTrue(false);
     	}
     	catch (NoSuchMethodException ex) {
     		assertTrue(false);
     	}
     	
    	visitor = new TestAdapter() {
    		public void visitRealArray(double[][] instances) {
    			assertEquals(2, instances.length);
    			assertEquals(3, instances[0].length);
    			assertEquals(3, instances[1].length);
    			for (int i = 0; i < 3; ++i) {
    				assertTrue( ((double[]) one[0])[i] == instances[0][i]);
    				assertTrue( ((double[]) two[0])[i] == instances[1][i]);
    			}
    			++count;
    		}
    		
    		public void visitIntegerArray(int[][] instances) {
    			assertEquals(2, instances.length);
    			assertEquals(2, instances[0].length);
    			assertEquals(2, instances[1].length);
    			for (int i = 0; i < 2; ++i) {
    				assertTrue( ((int[]) one[1])[i] == instances[0][i]);
    				assertTrue( ((int[]) two[1])[i] == instances[1][i]);
    			}
    			++count;
    		}
    		
    		/*public void checkMethodCalls() {
    			assertEquals(2, count);
    		}*/
    		
    		private int count = 0;
    		
    	};
    	
     	domain.acceptDomainVisitor(visitor, new Object[] { one , two });
     	try {
     		visitor.getClass().getMethod("checkMethodCalls", (Class[])null).invoke(visitor, (Object[])null);
     	}
     	catch (InvocationTargetException ex) {
     		assertTrue(false);
     	}
     	catch (IllegalAccessException ex) {
     		assertTrue(false);
     	}
     	catch (NoSuchMethodException ex) {
     		assertTrue(false);
     	}
     	
    }
    
    private ComponentFactory factory = ComponentFactory.instance();
    
    private class TestAdapter implements DomainVisitor {

		public void visitBit(Byte[] instances) {
			fail("visitBit() should not be called");
		}

		public void visitBitSet(BitSet[] instances) {
			fail("visitBitSet() should not be called");
		}

		public void visitReal(Double[] instances) {
			fail("visitReal() should not be called");
		}

		public void visitRealArray(double[][] instances) {
			fail("visitRealArray() should not be called");
		}

		public void visitInteger(Integer[] instances) {
			fail("visitInteger() should not be called");
		}

		public void visitIntegerArray(int[][] instances) {
			fail("visitIntegerArray() should not be called");
		}

		public void visitText(String[] instances) {
			fail("visitText() should not be called");
		}

		public void visitSet(Set[] instances) {
			fail("visitSet() should not be called");
		}

		public void visitUnknown(Object[] instances) {
			fail("visitUnknown() should not be called");
		}
    	
    }
    
}
