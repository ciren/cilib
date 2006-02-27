/*
 * Line.java
 *
 * Created on February 6, 2003, 9:24 PM
 *
 * 
 * Copyright (C) 2003 - Edwin S. Peer 
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

package net.sourceforge.cilib.Simulator;

import java.lang.*;

/**
 *
 * @author  espeer
 */
public class Line {
    
    /** Creates a new instance of Line */
    public Line(int elements) {
        this.elements = elements;
        count = 0;
        values = new Number[elements];
        for (int i = 0; i < elements; ++i) {
            values[i] = null;
        }
    }
    
    public void setElement(int index, Number value) {
        if (values[index] == null) {
            values[index] = value;
            ++count;
        }
    }
    
    public boolean isFull() {
        return (count == elements);
    }
    
    public String toString() {
        String line = "";
        for (int i = 0; i < elements; ++i) {
            line += values[i].toString() + " ";
        }
        return line;
    }
    
    private int elements;
    private int count;
    private Number[] values;
}
