/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public final class EntityIdFactory {
    private static ThreadLocal<AtomicLong> sequence = new ThreadLocal<AtomicLong>() {
        @Override
        protected AtomicLong initialValue() {
            return new AtomicLong(0);
        }
    };

    private EntityIdFactory() {
    }

    /**
     * Get the next number in the sequence as an {@code id} for the {@link Entity}. This
     * sequence is defined per thread and is defined to start counting from {@code 0}.
     * @return the next number in the sequence.
     */
    public static long getNextId() {
        AtomicLong instance = sequence.get();
        return instance.getAndIncrement();
    }

    public static void remove() {
        sequence.remove();
    }
}
