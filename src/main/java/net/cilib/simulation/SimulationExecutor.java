package net.cilib.simulation;

import java.util.concurrent.Future;

/**
 * @since 0.8
 * @author gpampara
 */
public interface SimulationExecutor {

    Future<Boolean> execute(Simulation runnable);
}
