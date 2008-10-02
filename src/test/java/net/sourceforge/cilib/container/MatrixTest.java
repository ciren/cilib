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

package net.sourceforge.cilib.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;



/**
 * This Unit test tests all the needed operations of the Matrix class.
 *
 * @author Gary Pampara
 */
public class MatrixTest {
	
	@Test
	public void testMatrixCreation() {
		Matrix<Double> m = new Matrix<Double>(3, 3);
		assertEquals(3, m.getColumnCount());
		assertEquals(3, m.getRowCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMatrixExceptionCreation() {
		new Matrix<Double>(0, 0);
	}

	@Test
	public void testDataMatrixInputOutputOperation() {
		Matrix<Integer> m = new Matrix<Integer>(10, 5);

		for (int i = 0; i < m.getRowCount(); i++) {
			for (int j = 0; j < m.getColumnCount(); j++) {
				int tmp = i*j;
				m.set(i, j, tmp);
				assertSame(tmp, m.get(i, j));
				assertEquals(tmp, m.get(i, j).intValue());
			}
		}
	}

	@Test
	public void testNullGet() {
		Matrix<Integer> m = new Matrix<Integer>(5, 5);

		for (int i = 0; i < m.getRowCount(); i++) {
			for (int j = 0; j < m.getColumnCount(); j++) {
				assertEquals(null, m.get(i, j));
			}
		}
	}

	@Test
	public void testOutOfBoundsSetOperation() {
		Matrix<Double> m = new Matrix<Double>(2, 2);
		try {
			m.set(-1, 1, null);
		}
		catch (IndexOutOfBoundsException i1) {
			try {
				m.set(1, -1, null);
			}
			catch (IndexOutOfBoundsException i2) {
				try {
					m.set(0, 2, null);
				}
				catch (IndexOutOfBoundsException i3) {
					try {
						m.set(2, 0, null);
					}
					catch (IndexOutOfBoundsException i4) {
						return;
					}
				}
			}
		}
		fail("Boundary cases fail on the Matrix set operation");
	}

	@Test
	public void testOutOfBoundsGetOperation() {
		Matrix<Double> m = new Matrix<Double>(2, 2);
		// Get operation
		try {
			m.get(-1, 1);			
		}
		catch (IndexOutOfBoundsException i) {
			try {
				m.get(1, -1);
			}
			catch (IndexOutOfBoundsException i2) {
				try {
					m.get(0, 2);
				}
				catch (IndexOutOfBoundsException i3) {
					try {
						m.get(2, 0);
					}
					catch (IndexOutOfBoundsException i4) {
						return;
					}
				}
			}
		}
		fail("Boundary cases fail on the Matrix get operation");
	}

	@Test
	public void testClearOperation() {
		Matrix<Double> m = new Matrix<Double>(2, 2);

		m.set(0, 0, new Double(5.0));
		m.set(0, 1, new Double(4.0));
		m.set(1, 0, new Double(3.0));
		m.set(1, 1, new Double(2.0));

		m.clear();

		assertEquals(null, m.get(0, 0));
		assertEquals(null, m.get(0, 1));
		assertEquals(null, m.get(1, 0));
		assertEquals(null, m.get(1, 1));
	}

	@Test
	public void testGetRow() {
		Matrix<Double> m = new Matrix<Double>(2, 2);

		m.set(0, 0, new Double(5.0));
		m.set(0, 1, new Double(4.0));
		m.set(1, 0, new Double(3.0));
		m.set(1, 1, new Double(2.0));

		Collection<Double> c1 = m.getRow(0);
		Collection<Double> c2 = m.getRow(1);

		Iterator<Double> i1 = c1.iterator();
		Iterator<Double> i2 = c2.iterator();
		
		for (int i = 0; i < m.getColumnCount(); i++) {
			if (i1.hasNext()) assertEquals(m.get(0, i), i1.next());
			else fail();
			
			if (i2.hasNext()) assertEquals(m.get(1, i), i2.next());
			else fail();
		}
	}

	@Test
	public void testGetColumn() {
		Matrix<Double> m = new Matrix<Double>(2, 2);

		m.set(0, 0, new Double(5.0));
		m.set(0, 1, new Double(4.0));
		m.set(1, 0, new Double(3.0));
		m.set(1, 1, new Double(2.0));

		Collection<Double> c1 = m.getColumn(0);
		Collection<Double> c2 = m.getColumn(1);
		
		Iterator<Double> i1 = c1.iterator();
		Iterator<Double> i2 = c2.iterator();

		for (int i = 0; i < m.getColumnCount(); i++) {
			if (i1.hasNext()) assertEquals(m.get(i, 0), i1.next());
			else fail();
			if (i2.hasNext()) assertEquals(m.get(i, 1), i2.next());
			else fail();
		}
	}
}
