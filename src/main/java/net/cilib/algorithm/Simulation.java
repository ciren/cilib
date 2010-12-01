package net.cilib.algorithm;

import com.google.common.base.Predicate;
import java.util.List;

/**
 * {@code Simulation} provides an abstraction for any type of simulation. For
 * example, a {@code PopulationBasedAlgorithm} running on a given problem with
 * a set of defined measurements.
 * @author gpampara
 */
public interface Simulation {

    void execute(List<Predicate<Algorithm>> stoppingConditions);

    static interface Builder {
        Simulation build();
    }
}
