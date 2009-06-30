/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.simulator;


/**
 *
 * @author  Edwin Peer
 */
class Line {

    /** Creates a new instance of Line. */
    public Line(int elements) {
        this.elements = elements;
        count = 0;
        values = new Object[elements];
        for (int i = 0; i < elements; ++i) {
            values[i] = null;
        }
    }

    /**
     * Set the element at the given index.
     * @param index The position to set the value.
     * @param value The value to set.
     */
    public void setElement(int index, Object value) {
        if (values[index] == null) {
            values[index] = value;
            ++count;
        }
    }

    /**
     * Determine if the current line is full.
     * @return {@code true} if the line is full, {@code false} otherwsie.
     */
    public boolean isFull() {
        return (count == elements);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String toString() {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < elements; ++i) {
            if (values[i] != null) {
                line.append(values[i].toString());
            }
            else {
                line.append("<null>");
            }
            line.append(" ");
        }
        return line.toString();
    }

    private int elements;
    private int count;
    private Object[] values;
}
