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
package net.sourceforge.cilib.clustering.kmeans;

import java.util.ArrayList;
import java.util.Collections;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Monitor the {@link #previousCentroids centroids} for moments of stagnation and add diversity
 * ({@link VectorUtils#jitter(vector, ratio) jitter}) to each individual centroid when the specific centroid has not
 * changed for {@link #interval} number of iterations. The amount of diversity added is
 * calculated by the {@link VectorUtils#jitter(net.sourceforge.cilib.type.types.container.Vector, double)} method.
 *
 * @author theuns.cloete
 */
public class StandardCentroidsDiversificationStrategy implements CentroidsDiversificationStrategy {
    private static final long serialVersionUID = -5900302560150700540L;

    private ArrayList<Vector> previousCentroids;
    private ArrayList<Integer> unchangedIterations;
    private double diversifyRatio;
    private int interval;

    /**
     * Construct the diversification strategy. The default {@link #diversifyRatio} is 0.1 and the default
     * {@link #interval} is 10 iterations.
     */
    public StandardCentroidsDiversificationStrategy() {
        this.diversifyRatio = 0.1;
        this.interval = 10;
    }

    public StandardCentroidsDiversificationStrategy(StandardCentroidsDiversificationStrategy rhs) {
        this.previousCentroids = new ArrayList<Vector>();

        for (Vector previousCentroid : rhs.previousCentroids) {
            this.previousCentroids.add(Vector.copyOf(previousCentroid));
        }

        this.unchangedIterations = new ArrayList<Integer>();
        this.unchangedIterations.addAll(rhs.unchangedIterations);
        this.diversifyRatio = rhs.diversifyRatio;
        this.interval = rhs.interval;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardCentroidsDiversificationStrategy getClone() {
        return new StandardCentroidsDiversificationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(ArrayList<Vector> centroids) {
        this.previousCentroids = new ArrayList<Vector>(centroids.size());

        for (Vector centroid : centroids) {
            this.previousCentroids.add(Vector.copyOf(centroid));
        }

        this.unchangedIterations = new ArrayList<Integer>(centroids.size());
        this.unchangedIterations.addAll(Collections.nCopies(centroids.size(), 0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void diversify(ArrayList<Vector> currentCentroids, int which) {
        Vector currentCentroid = currentCentroids.get(which);
        Vector previousCentroid = this.previousCentroids.get(which);
        int unchanged = this.unchangedIterations.get(which);

        if (currentCentroid.equals(previousCentroid)) {
            this.unchangedIterations.set(which, ++unchanged);
        }
        else {
            this.previousCentroids.set(which, Vector.copyOf(currentCentroid));
            this.unchangedIterations.set(which, 0);
        }

        if (unchanged >= this.interval) {
            currentCentroid = Vectors.jitter(currentCentroid, this.diversifyRatio);
            currentCentroids.set(which, currentCentroid);
            this.previousCentroids.set(which, Vector.copyOf(currentCentroid));
            this.unchangedIterations.set(which, 0);
        }
    }

    /**
     * Get the current diversify ratio.
     * @return The current {@link #diversifyRatio}.
     */
    public double getDiversifyRatio() {
        return this.diversifyRatio;
    }

    /**
     * Set the current diversify ratio.
     * @param dr The {@link #diversifyRatio} to use.
     */
    public void setDiversifyRatio(double dr) {
        this.diversifyRatio = dr;
    }

    /**
     * Get the current interval.
     * @return The current {@link #interval}.
     */
    public int getInterval() {
        return this.interval;
    }

    /**
     * Set the current interval.
     * @param i The {@link #interval} to use.
     */
    public void setInterval(int i) {
        this.interval = i;
    }
}
