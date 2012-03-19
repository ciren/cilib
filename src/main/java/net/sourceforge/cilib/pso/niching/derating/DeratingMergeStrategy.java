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
package net.sourceforge.cilib.pso.niching.derating;

import java.util.ArrayList;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 */
public class DeratingMergeStrategy {

    private double threshold;

    public DeratingMergeStrategy() {
        this.threshold  = 0.001;
    }

    public DeratingMergeStrategy(DeratingMergeStrategy copy) {
        this.threshold = copy.threshold;
    }

    public void merge(final ArrayList<Particle> algorithm) {
        if (algorithm.size() < 2)
            return;

        DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();

        ArrayList<Particle> temp = new ArrayList<Particle>();
        temp.add(algorithm.get(0));
        boolean found;
        for (int i = 1; i < algorithm.size()-1; i++) {
            Particle k1 = algorithm.get(i);
            found = false;

            for (int j = 0; j < temp.size(); j++) {
                Particle k2 = temp.get(j);

                Vector vectorK1 = (Vector) k1.getBestPosition();
                Vector vectorK2 = (Vector) k2.getBestPosition();

                Vector normalK1 = vectorK1;
                Vector normalK2 = vectorK2;

                double distance = distanceMeasure.distance(normalK1, normalK2);//Math.abs(normalK1.subtract(normalK2).norm());

                if (distance < threshold) {
                    found =true;
                }
            }

            if(!found){
                temp.add(k1);
            }
        }
        algorithm.clear();
        for(int i=0; i<temp.size(); i++){
            algorithm.add(temp.get(i));
        }
    }

    /**
     * Get the merge threshold value.
     * @return The value of the merge threshold.
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * Set the merge threshold value.
     * @param threshold The value to set.
     */
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

}
