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

package net.sourceforge.cilib.functions.continuous;

import java.util.ArrayList;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 */
public class DeratingFunction implements ContinuousFunction{

    private double radius;
    private double alpha;
    private ArrayList<Vector> ybests;
    private DistanceMeasure distanceMeasure;
    private ContinuousFunction previousFunction;

    public DeratingFunction(){
        distanceMeasure = new EuclideanDistanceMeasure();
        radius = 0.5;
        alpha = 0.8;
    }

    public ContinuousFunction getClone() {
        return (ContinuousFunction) new DeratingFunction();
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * @return the alpha
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * @param alpha the alpha to set
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * @param ybests the ybests to set
     */
    public void setYbests(ArrayList<Vector> ybests) {
        this.ybests = ybests;
    }

    /**
     * @param previousFunction the previousFunction to set
     */
    public void setPreviousFunction(ContinuousFunction previousFunction) {
        this.previousFunction = previousFunction;
    }

    @Override
    public Double apply(Vector input) {
        double product = 1.0;
        double distance;
        for(Vector yhead: ybests){
            distance = distanceMeasure.distance(input, yhead);

            if(distance < getRadius()){
                product *= Math.pow(distance/getRadius(), getAlpha());
            }
        }
        double m = previousFunction.apply(input);
        return (product * m);
    }

}
