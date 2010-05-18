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
package net.sourceforge.cilib.algorithm;

/**
 * A progress event informs the listening party about the percentage complete of the simulation.
 *
 * @author  Edwin Peer
 */
public class ProgressEvent {

    /**
     * Creates a new instance of ProgressEvent with a given completion percentage.
     * @param percentage The current percentage value.
     */
    public ProgressEvent(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Accessor for the percentage completed.
     * @return The percentage
     */
    public double getPercentage() {
        return percentage;
    }

    private double percentage;
}
