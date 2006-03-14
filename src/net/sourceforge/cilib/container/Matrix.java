/*
 * Matrix.java
 *
 * Created on Aug 17, 2004
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
package net.sourceforge.cilib.container;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.ArrayList;
import java.lang.IndexOutOfBoundsException;

/**
 * Represenstation of a <code>Matrix</code>, with the rows and the columns represented as a 2D array. The 2D array has been implemented as a Object array as the needed data stored within the Matrix is variable
 * 
 * @author Gary Pampara
 */
public class Matrix<E> implements Cloneable {
	private ArrayList< ArrayList<E> > data; // This is the ArrayList of the 1st dimension
	private int rows;
	private int cols;
	
	/**
	 * Create a new <code>Matrix</code> object with dimensions: rows x columns
	 * @param rows The number of rows the <code>Matrix</code> should contain
	 * @param cols The number of columns the <code>Matrix</code> should contain
	 */
	public Matrix(int rows, int cols) {
		if (rows == 0 || cols == 0) {
			throw new IllegalArgumentException("Cannot create a Matrix with row or column dimension < 1");
		}
		
		this.rows = rows;
		this.cols = cols;

		data = new ArrayList< ArrayList<E> >();

		for (int i = 0; i < rows; i++) {
			ArrayList<E> tmp = new ArrayList<E>();

			for (int j = 0; j < cols; j++) {
				tmp.add(null);
			}

			data.add(tmp);
		}
	}
	
	
	/**
	 * TODO: Unit test to check this
	 * Copy constructor
	 * @param copy
	 */
	public Matrix(Matrix copy) {
		rows = copy.rows;
		cols = copy.cols;
		
		data = new ArrayList<ArrayList<E>>();
		
		for (Iterator<ArrayList<E>> i = data.iterator(); i.hasNext(); ) {
			ArrayList<E> cloneList = new ArrayList<E>();
			ArrayList<E> tmp = i.next();
			
			for (Iterator<E> j = tmp.iterator(); j.hasNext(); ) {
				E item = j.next();
				cloneList.add(item);
			}
		}
	}
	
	
	public Matrix clone() {
		return new Matrix(this);
	}

	/**
	 * Place an <code>Object</code> at a point (row, column) within the <code>Matrix</code>
	 * @param row The row where the needed item is located
	 * @param col The column where the needed item is located
	 * @param object The <code>Object</code> to place the <code>Matrix</code> at prosition (row, column)
	 */
	public void set(int row, int col, E object) {
		if ((row >= rows || col >= cols) && (row >= 0 || col >= 0)) {
			throw new IndexOutOfBoundsException("Cannot set item at out of bounds index");
		}

		ArrayList<E> tmp = data.get(row);
		tmp.set(col, object);
	}

	/**
	 * Return the current item within the grid, located at (row, column)
	 * @param row The row where the needed item is located
	 * @param col The column where the needed item is located
	 * @return The <code>Object</code> within the <code>Matrix</code> at prosition (row, column)
	 */
	public E get(int row, int col) {
		if ((row >= rows || col >= cols) && (row >= 0 || col >= 0)) {
			throw new IndexOutOfBoundsException("Cannot acces element - index out of bounds");
		}

		return data.get(row).get(col);
	}

	/**
	 * Get the number of columns in the <code>Matrix</code>
	 * @return The number of columns in the <code>Matrix</code> 
	 */
	public int getColumnCount() {
		return cols; 
	}

	/**
	 * Get the number of rows in the <code>Matrix</code>
	 * @return The number of rows in the <code>Matrix</code> 
	 */
	public int getRowCount() {
		return rows;
	}

	/**
	 * Test wheather the current matrix equals a given <code>Matrix</code>
	 * @param target The <code>Matrix</code> to have the current <code>Matrix</code> evaluated against
	 * @return <code>true</code> if the matricies are equal
	 * @return <code>false</code> if the matricies are not equal
	 */
	public boolean equals(Matrix target) {
		return false;
	}

	/**
	 * Clear the current <code>Matrix</code> of it's internal state
	 */
	public void clear() {
		data = null;
		data = new ArrayList< ArrayList<E> >();

		for (int i = 0; i < rows; i++) {
			ArrayList<E> tmp = new ArrayList<E>();

			for (int j = 0; j < cols; j++) {
				tmp.add(null);
			}
			
			data.add(tmp);
		}
	}

	/**
	 * Get a <code>Vector</code> representing the row within the <code>Matrix</code> at the given index
	 * @param row The row index of the row to be returned, indexed from 0
	 * @return A <code>Vector</code> representing the row within the <code>Matrix</code> 
	 */
	public Collection<E> getRow(int row) {
		ArrayList<E> tmp = new ArrayList<E>();

		for (int i = 0; i < cols; i++) {
			tmp.add(this.get(row, i));
		}

		return tmp;
	}

	/**
	 * Get a <code>Vector</code> representing the column within the <code>Matrix</code> at the given index
	 * @param col The column index of the row to be returned, indexed from 0
	 * @return A <code>Vector</code> representing the column within the <code>Matrix</code>
	 */
	public Collection<E> getColumn(int col) {
		Collection<E> tmp = new Vector<E>();

		for (int i = 0; i < rows; i++)
			tmp.add(this.get(i, col));

		return tmp;
	}
	


	/**
	 * This method multiplies the current matrix by the given matrix, provided such an operation is
	 * permitted.
	 * 
	 * @param target The target matrix to multiply this matrix against
	 * @return A Matrix representing the product from the original two matricies
	 */
	/*	public Matrix multiply(Matrix target) {
			Matrix newMatrix = new Matrix(this.getColumnCount(), target.getRowCount()); // m x n

	// TODO: Finsih this off (Brush up on Linear Algebra)

	return newMatrix;
	}*/

	/**
	 * Get the identity matrix of matrix with the size (rows, cols)
	 * 
	 * @param rows The number of rows in the martix
	 * @param cols The number of columns in the matrix 
	 * @return A <code>Matrix</code> object containing an identity matrix
	 */
	/*	public static Matrix getIdentityMatrix(int rows, int cols) {
			int i = 0;
			int j = 0;
			Matrix newMatrix = new Matrix(rows, cols);

			while (i != (rows-1) && j != (cols-1) ) {
			newMatrix.setItemAt(i, j, new Double(1.0));			
			}

			return newMatrix;
			}*/
}
