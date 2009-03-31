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

import java.io.Serializable;
import java.util.ArrayList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Monitor centroids for moments of stagnation and add diversity as and when desired.
 *
 * @author theuns.cloete
 */
public interface CentroidsDiversificationStrategy extends Serializable, Cloneable {
    /**
     * {@inheritDoc}
     */
    @Override
    public CentroidsDiversificationStrategy getClone();

    /**
     * Initialise this centroids diversification strategy based on the given {@link ArrayList} of centroid
     * {@link Vector}s.
     *
     * @param centroids The {@link ArrayList} containing all the centroids.
     */
    public void initialise(ArrayList<Vector> centroids);

    /**
     * Diversify the specified centroid in the given list of centroids. Note that a diversification will only happen
     * based on the rules implemented in the classes that override this method.
     * @param centroids The {@link ArrayList} containing all the centroids.
     * @param which Which centroid (of the given centroids) should be diversified?
     */
    public void diversify(ArrayList<Vector> centroids, int which);
}
