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
package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import net.sourceforge.cilib.neuralnetwork.foundation.Initializable;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.type.types.container.Vector;




/**
 * @author stefanv
 *
 */
public class StandardPattern implements NNPattern, Initializable {
    private static final long serialVersionUID = 6917164648392481566L;
    protected Vector input = null;
    protected Vector target = null;



    public StandardPattern() {
        super();
        this.input = null;
        this.target = null;
    }



    public StandardPattern(Vector input, Vector target) {
        super();
        this.input = input;
        this.target = target;
    }



    public void initialize(){
        if ((this.input == null)|| (this.target == null)){
            throw new IllegalArgumentException("Required object was null during initialization");
        }
    }



    public Vector getInput() {
        return input;
    }


    public int getInputLength() {
        return input.size();
    }


    public int getTargetLength() {
        return target.size();
    }


    public Vector getTarget() {
        return target;
    }

    public NNPattern getClone(){
        StandardPattern tmp = new StandardPattern();
        tmp.setInput(this.input);
        tmp.setTarget(this.target);
        return tmp;
    }

    public String toString(){
        String tmp = new String();
        tmp += "{";
        for (int i = 0; i < input.size(); i++){
            tmp+= (input.get(i) + " ");
        }
        tmp += " \t| ";
        for (int i = 0; i < target.size(); i++){
            tmp += (target.get(i) + " ");
        }
        tmp += "}";
        return tmp;
    }

    public void setInput(Vector input) {
        this.input = input;
    }

    public void setTarget(Vector target) {
        this.target = target;
    }




}
