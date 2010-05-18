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
package net.sourceforge.cilib.simulator;

import java.io.File;
import java.util.List;

/**
 * Combines a collection of smaller data sources into a single datasource.
 */
public interface MeasurementCombiner {

    /**
     * Combine many partial results into a single result file. Once
     * this method completes, all streams will be closed for the provided files.
     * @param descriptions header descriptions for the results.
     * @param partials list of partial results.
     */
    public abstract void combine(List<String> descriptions, List<File> partials);
}
