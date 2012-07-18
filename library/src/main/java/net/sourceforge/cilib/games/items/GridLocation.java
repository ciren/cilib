/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.games.items;

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * A location inside a grid
 */
public class GridLocation implements ItemLocation {

    private static final long serialVersionUID = 4974578979607886491L;
    private Vector delegate;

    public GridLocation(int gridWidth, int gridHeight) {
        delegate = Vector.of(Int.valueOf(0, new Bounds(0, gridWidth)), 
                Int.valueOf(0, new Bounds(0, gridHeight)));
    }

    public GridLocation(int xMax, int yMax, int zMax) {
        delegate = Vector.of(Int.valueOf(0, new Bounds(0, xMax)),
                Int.valueOf(0, new Bounds(0, yMax)),
                Int.valueOf(0, new Bounds(0, zMax)));
    }

    /**
     * @param Other
     */
    public GridLocation(GridLocation other) {
        this.delegate = Vector.copyOf(other.delegate);
    }

    /**
     * Set the position of this GridLocation to the given vector
     * @param newPos the specified position
     */
    public void setPosition(Vector newPos) {
        if (newPos.size() != delegate.size()) {
            throw new UnsupportedOperationException("Cannot set the postition to a vector with a different dimention");
        }
        
        Vector.Builder builder = Vector.newBuilder();
        for (Numeric t : newPos) {
            builder.add(t);
        }
        delegate = builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GridLocation getClone() {
        return new GridLocation(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDistance(DistanceMeasure measure, ItemLocation other) {
        if (!(other instanceof GridLocation)) {
            throw new RuntimeException("can only determine the distance between two gridlocation itemlocations");
        }
        return measure.distance(delegate, ((GridLocation) other).delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveItem(Type amount) {
        if (!(amount instanceof Vector)) {
            throw new RuntimeException("can only add a vector to gridlocation");
        }
        for (int i = 0; i < ((Vector) amount).size(); ++i) {

            int newVal = ((Vector) amount).intValueOf(i) + delegate.intValueOf(i);
            if (newVal < delegate.get(i).getBounds().getLowerBound()) {
                newVal = (int) delegate.get(i).getBounds().getLowerBound();
            } else if (newVal >= delegate.get(i).getBounds().getUpperBound()) {
                newVal = (int) delegate.get(i).getBounds().getUpperBound() - 1;
            }
            delegate.setInt(i, newVal);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof GridLocation) {
            return super.equals(((GridLocation) other));
        }
        return false;
    }

    public void setInt(int i, int i0) {
        delegate.setInt(i, i0);
    }

    public int intValueOf(int i) {
        return delegate.intValueOf(i);
    }

    public Vector getLocation() {
        return delegate;
    }
}
