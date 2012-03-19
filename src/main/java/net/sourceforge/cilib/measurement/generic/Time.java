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
package net.sourceforge.cilib.measurement.generic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.AlgorithmEvent;
import net.sourceforge.cilib.algorithm.AlgorithmListener;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Int;

/**
 */
public class Time implements Measurement<Int>, AlgorithmListener {

    private static final long serialVersionUID = -3516066813688827758L;
    private boolean running = false;
    private long startTime;
    private long endTime;

    /**
     * Create a default instance of {@linkplain Time}.
     */
    public Time() {
        running = false;
        startTime = System.currentTimeMillis();
        endTime = startTime;
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public Time(Time copy) {
        running = copy.running;
        startTime = copy.startTime;
        endTime = copy.endTime;
    }

    /**
     * {@inheritDoc}
     */
    public Time getClone() {
        return new Time(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return "Z";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int getValue(Algorithm algorithm) {
        if (running) {
            return Int.valueOf(Long.valueOf(System.currentTimeMillis() - startTime).intValue());
        } else {
            return Int.valueOf(Long.valueOf(endTime - startTime).intValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void algorithmStarted(AlgorithmEvent e) {
        running = true;
        startTime = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    public void algorithmFinished(AlgorithmEvent e) {
        endTime = System.currentTimeMillis();
        running = false;
    }

    /**
     * {@inheritDoc}
     */
    public void algorithmTerminated(AlgorithmEvent e) {
        endTime = System.currentTimeMillis();
        running = false;
    }

    /**
     * {@inheritDoc}
     */
    public void iterationCompleted(AlgorithmEvent e) {
    }
}
