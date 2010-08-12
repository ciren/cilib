package net.cilib.simulation;

import com.google.inject.Inject;
import java.util.List;
import net.cilib.algorithm.Algorithm;
import net.cilib.measurement.Measurement;
import net.cilib.problem.Problem;

/**
 * @since 0.8
 * @author gpampara
 */
public final class Simulation {

    private final Algorithm algorithm;
    private final Problem problem;
    private final List<Measurement> measurements;

    @Inject
    public Simulation(/*@Assisted*/ Algorithm alg,
            /*@Assisted*/ Problem prob,
            /*@Assisted*/ List<Measurement> measurements) {
        this.algorithm = alg;
        this.problem = prob;
        this.measurements = measurements;
    }
}
