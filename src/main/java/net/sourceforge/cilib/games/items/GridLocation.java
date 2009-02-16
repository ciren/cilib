/*
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
package net.sourceforge.cilib.games.items;

import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.TypeUtil;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * @author leo
 * A location inside a grid
 */
public class GridLocation extends ItemLocation {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4974578979607886491L;
	Vector position;
	/**
	 * 
	 */
	public GridLocation(int gridDimention, int gridWidth, int gridHeight) {
		position = new Vector(gridDimention);			
		position.add(new Int(0, gridWidth));
		position.add(new Int(0, gridHeight));
	}

	/**
	 * @param Other
	 */
	public GridLocation(GridLocation other) {
		super(other);
		// TODO Auto-generated constructor stub
		position = new Vector(other.position);
	}
	
	public void Move(Vector coords){
		moveItem(coords);
	}
	
	public void setPosition(Vector newPos){
		if(newPos.getDimension() != position.getDimension()){
			throw new UnsupportedOperationException("Cannot set the postition to a vector with a different dimention");
		}
		position = newPos;
	}
	
	public Vector getPosition(){
		return position;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.games.items.ItemLocation#getClone()
	 */
	@Override
	public GridLocation getClone() {
		// TODO Auto-generated method stub
		return new GridLocation(this);
	}
	
/*	@Override
	public Type getLocationData(){
		return position;
	}*/

	@Override
	public Double getDistance(DistanceMeasure measure, ItemLocation other) {
		if(!(other instanceof GridLocation))
			throw new RuntimeException("can only determine the distance between two gridlocation itemlocations");
		Vector vector = ((GridLocation)other).getPosition();
		double result = measure.distance(position, vector);

		return result;
	}

	@Override
	public void moveItem(Type amount) {
		if(!(amount instanceof Vector))
			throw new RuntimeException("can only add a vector to gridlocation");

		Vector amountVector = (Vector) amount;
		Vector np = position.getClone();
		for(int i = 0; i < amountVector.size(); ++i){
			np.setInt(i, amountVector.getInt(i) + position.getInt(i));
		}
		
		if(TypeUtil.isInsideBounds(np)){
			position = np;
		}
	}

}
