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
package net.sourceforge.cilib.games.items;

import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * @author leo
 * A location inside a grid
 */
public class GridLocation extends Vector implements ItemLocation {
    private static final long serialVersionUID = 4974578979607886491L;
    public GridLocation(int gridWidth, int gridHeight) {
        this.add(new Int(0, gridWidth));
        this.add(new Int(0, gridHeight));
    }

    public GridLocation(int xMax, int yMax, int zMax) {
        this.add(new Int(0, xMax));
        this.add(new Int(0, yMax));
        this.add(new Int(0, zMax));
    }

    /**
     * @param Other
     */
    public GridLocation(GridLocation other) {
        super(other);
    }
    /**
     * Set the position of this GridLocation to the given vector
     * @param newPos the specified position
     */
    public void setPosition(Vector newPos){
        if(newPos.getDimension() != this.getDimension()){
            throw new UnsupportedOperationException("Cannot set the postition to a vector with a different dimention");
        }
        this.clear();
        for(Numeric t : newPos){
            this.add(t.getClone());
        }
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
        if(!(other instanceof GridLocation))
            throw new RuntimeException("can only determine the distance between two gridlocation itemlocations");
        return measure.distance(this, ((GridLocation) other));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveItem(Type amount) {
        if(!(amount instanceof Vector))
            throw new RuntimeException("can only add a vector to gridlocation");
        for(int i = 0; i < ((Vector)amount).size(); ++i){

            int newVal = ((Vector)amount).getInt(i) + this.getInt(i);
            if(newVal < this.get(i).getBounds().getLowerBound())
                newVal = (int)this.get(i).getBounds().getLowerBound();
            else if(newVal > this.get(i).getBounds().getUpperBound())
                newVal = (int)this.get(i).getBounds().getUpperBound();
            this.setInt(i, newVal);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof GridLocation)
            return super.equals(((GridLocation)other));
        return false;
    }
}
