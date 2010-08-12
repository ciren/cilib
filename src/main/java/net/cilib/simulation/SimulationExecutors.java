package net.cilib.simulation;

/**
 * @since 0.8
 * @author gpampara
 */
public final class SimulationExecutors {

    private SimulationExecutors() {
    }

    public static SimulationExecutor newFixedThreadExecutor() {
        return new FixedThreadSimulationExecutor(Runtime.getRuntime().availableProcessors());
    }
}
