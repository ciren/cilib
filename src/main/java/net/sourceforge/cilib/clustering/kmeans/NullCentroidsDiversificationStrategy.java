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
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This strategy is needed so that {@link KMeans} can behave as defined in the original article.
 * All the methods are empty, because nothing needs to be done.
 *
 * @author Theuns Cloete
 */
public class NullCentroidsDiversificationStrategy implements CentroidsDiversificationStrategy {
    /**
     * {@inheritDoc}
     */
    @Override
    public CentroidsDiversificationStrategy getClone() {
        return this;
    }

    /**
     * No initialisation is needed, because nothing needs to be done. This is an empty method.
     */
    @Override
    public void initialise(ArrayList<Vector> centroids) {
    }

    /**
     * No diversification is needed, because nothing needs to be done. This is an empty method.
     */
    @Override
    public void diversify(ArrayList<Vector> centroids, int which) {
    }
}
