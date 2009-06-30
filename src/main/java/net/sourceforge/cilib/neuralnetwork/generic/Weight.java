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
package net.sourceforge.cilib.neuralnetwork.generic;

import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Types;
import net.sourceforge.cilib.util.Cloneable;

/**
 * @author stefanv
 *
 */
public class Weight implements Cloneable {
    private static final long serialVersionUID = -1229158606594188854L;

    Type weightValue = null;

    //a multi-purpose variable to track changes - value/menaingdepends on the using class
    Type previousChange = null;

    /**
     * @param weightValue
     */
    public Weight() {
        this.weightValue = null;
        this.previousChange = null;
    }


    public Weight(Type w){
        super();
        this.weightValue = w.getClone();
        this.previousChange = w.getClone();
        Types.reset(this.previousChange);
//        this.previousChange.reset();
    }


    public Weight getClone(){
        Weight clone = new Weight(this.weightValue);
        clone.previousChange = this.previousChange;
        return clone;
    }


    public Type getWeightValue() {
        return weightValue;
    }

    public void setWeightValue(Type weightValue) {
        this.weightValue = weightValue;
    }


    public Type getPreviousChange() {
        return previousChange;
    }


    public void setPreviousChange(Type previousChange) {
        this.previousChange = previousChange;
    }


}
