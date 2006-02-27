/*
 * Matrix.java
 *
 * Created on Aug 17, 2004
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
 */
package net.sourceforge.cilib.Container;

import java.util.Vector;

/**
 * @author gpampara
 */
public class Matrix {
	private Object[][] data;
	private int rows;
	private int cols;
	
	public Matrix(int rows, int cols) {
		data = new Object[rows][cols];
	}
	
	public void setItemAt(int row, int col, Object object) {
		data[row][col] = object;
	}
	
	public Object getItemAt(int row, int col) {
		return data[row][col];
	}
	
	public int getColumnCount() {
		return cols; 
	}
	
	public int getRowCount() {
		return rows;
	}
	
	public boolean equals(Matrix target) {
		return false;
	}
	
	public void clear() {
		data = null;
		data = new Object[rows][cols];
	}
	
	public Vector getRow(int row) {
		Vector tmp = new Vector();
		
		for (int i = 0; i < cols; i++) {
			tmp.add(getItemAt(row, i));
		}
		
		return tmp;
	}
	
	public Vector getColumn(int col) {
		Vector tmp = new Vector();
		
		for (int i = 0; i < rows; i++)
			tmp.add(getItemAt(i, col));
		
		return tmp;
	}
	
	/**
	 * This method multiplies the current matrix by the given matrix, provided such an operation is
	 * permitted.
	 * 
	 * @param target The target matrix to multiply this matrix against
	 * @return A Matrix representing the product from the original two matricies
	 */
	public Matrix multiply(Matrix target) {
		Matrix newMatrix = new Matrix(this.getColumnCount(), target.getRowCount()); // m x n
		
		return newMatrix;
	}
	
	public static Matrix getIdentityMatrix(int rows, int cols) {
		int i = 0;
		int j = 0;
		Matrix newMatrix = new Matrix(rows, cols);
		
		while (i != (rows-1) && j != (cols-1) ) {
			newMatrix.setItemAt(i, j, new Double(1.0));			
		}
		
		return newMatrix;
	}
}
