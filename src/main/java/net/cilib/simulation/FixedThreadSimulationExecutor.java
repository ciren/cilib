package net.cilib.simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @since 0.8
 * @author gpampara
 */
public class FixedThreadSimulationExecutor implements SimulationExecutor {

    private final ExecutorService executor;

    public FixedThreadSimulationExecutor(int availableProcessors) {
        this.executor = Executors.newFixedThreadPool(availableProcessors);
    }

    @Override
    public Future<Boolean> execute(final Simulation runnable) {
        return (Future<Boolean>) this.executor.submit(new Runnable() {
            @Override
            public void run() {
//                runnable.
            }
        });
    }
}
