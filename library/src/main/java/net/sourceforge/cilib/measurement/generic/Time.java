/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
    @Override
    public Time getClone() {
        return new Time(this);
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
    @Override
    public void algorithmStarted(AlgorithmEvent e) {
        running = true;
        startTime = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmFinished(AlgorithmEvent e) {
        endTime = System.currentTimeMillis();
        running = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void iterationCompleted(AlgorithmEvent e) {
    }
}
