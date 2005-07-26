/*
 * MatrixTest.java
 * JUnit based test
 *
 * Created on January 04, 2005, 4:45 PM
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

package net.sourceforge.cilib.Container;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Vector;

/**
 * This Unit test tests all the needed operations of the Matrix class.
 *
 * @author gpampara
 */
public class MatrixTest extends TestCase {

	public MatrixTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(MatrixTest.class);

		return suite;
	}

	public void setUp() {
	}

	public void testMatrixCreation() {
		Matrix<Double> m = new Matrix<Double>(3, 3);
		assertEquals(3, m.getColumnCount());
		assertEquals(3, m.getRowCount());
	}

	public void testMatrixExceptionCreation() {
		try {
			Matrix<Double> m = new Matrix<Double>(0, 0);
			assertNotNull(m);
		}
		catch (IllegalArgumentException i) {
			return;
		}
		fail("Expected IllegalArgumentException");
	}

	public void testDataMatrixInputOutputOperation() {
		Matrix<Integer> m = new Matrix<Integer>(10, 5);

		for (int i = 0; i < m.getRowCount(); i++) {
			for (int j = 0; j < m.getColumnCount(); j++) {
				Integer tmp = new Integer(i*j);
				m.setItemAt(i, j, tmp);
				assertSame(tmp, m.getItemAt(i, j));
				assertEquals(tmp.intValue(), m.getItemAt(i, j).intValue());
			}
		}
	}

	public void testNullGet() {
		Matrix<Integer> m = new Matrix<Integer>(5, 5);

		for (int i = 0; i < m.getRowCount(); i++) {
			for (int j = 0; j < m.getColumnCount(); j++) {
				assertEquals(null, m.getItemAt(i, j));
			}
		}
	}

	public void testOutOfBoundsSetOperation() {
		Matrix<Double> m = new Matrix<Double>(2, 2);
		try {
			m.setItemAt(-1, 1, null);
		}
		catch (IndexOutOfBoundsException i1) {
			try {
				m.setItemAt(1, -1, null);
			}
			catch (IndexOutOfBoundsException i2) {
				try {
					m.setItemAt(0, 2, null);
				}
				catch (IndexOutOfBoundsException i3) {
					try {
						m.setItemAt(2, 0, null);
					}
					catch (IndexOutOfBoundsException i4) {
						return;
					}
				}
			}
		}
		fail("Boundary cases fail on the Matrix set operation");
	}

	public void testOutOfBoundsGetOperation() {
		Matrix<Double> m = new Matrix<Double>(2, 2);
		// Get operation
		try {
			m.getItemAt(-1, 1);			
		}
		catch (IndexOutOfBoundsException i) {
			try {
				m.getItemAt(1, -1);
			}
			catch (IndexOutOfBoundsException i2) {
				try {
					m.getItemAt(0, 2);
				}
				catch (IndexOutOfBoundsException i3) {
					try {
						m.getItemAt(2, 0);
					}
					catch (IndexOutOfBoundsException i4) {
						return;
					}
				}
			}
		}
		fail("Boundary cases fail on the Matrix get operation");
	}

	public void testClearOperation() {
		Matrix<Double> m = new Matrix<Double>(2, 2);

		m.setItemAt(0, 0, new Double(5.0));
		m.setItemAt(0, 1, new Double(4.0));
		m.setItemAt(1, 0, new Double(3.0));
		m.setItemAt(1, 1, new Double(2.0));

		m.clear();

		assertEquals(null, m.getItemAt(0, 0));
		assertEquals(null, m.getItemAt(0, 1));
		assertEquals(null, m.getItemAt(1, 0));
		assertEquals(null, m.getItemAt(1, 1));
	}

	public void testGetRow() {
		Matrix<Double> m = new Matrix<Double>(2, 2);

		m.setItemAt(0, 0, new Double(5.0));
		m.setItemAt(0, 1, new Double(4.0));
		m.setItemAt(1, 0, new Double(3.0));
		m.setItemAt(1, 1, new Double(2.0));

		Vector<Double> v1 = m.getRow(0);
		Vector<Double> v2 = m.getRow(1);

		for (int i = 0; i < m.getColumnCount(); i++) {
			assertEquals(m.getItemAt(0, i), v1.elementAt(i));
			assertEquals(m.getItemAt(1, i), v2.elementAt(i));
		}
	}

	public void testGetColumn() {
		Matrix<Double> m = new Matrix<Double>(2, 2);

		m.setItemAt(0, 0, new Double(5.0));
		m.setItemAt(0, 1, new Double(4.0));
		m.setItemAt(1, 0, new Double(3.0));
		m.setItemAt(1, 1, new Double(2.0));

		Vector<Double> v1 = m.getColumn(0);
		Vector<Double> v2 = m.getColumn(1);

		for (int i = 0; i < m.getColumnCount(); i++) {
			assertEquals(m.getItemAt(i, 0), v1.elementAt(i));
			assertEquals(m.getItemAt(i, 1), v2.elementAt(i));
		}
	}
}
