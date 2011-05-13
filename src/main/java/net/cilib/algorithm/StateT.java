package net.cilib.algorithm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class defines the global algorithm state associated with a running
 * algorithm.
 * @author gpampara
 */
public class StateT {

    private AtomicInteger iterations = new AtomicInteger();

    public void incIterations() {
        this.iterations.incrementAndGet();
    }

    public int iterations() {
        return iterations.get();
    }
}
