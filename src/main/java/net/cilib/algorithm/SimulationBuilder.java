package net.cilib.algorithm;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.inject.Inject;
import com.google.inject.Key;
import java.util.List;
import net.cilib.collection.Topology;
import net.cilib.entity.Entity;
import net.cilib.inject.SimulationScope;
import net.cilib.measurement.Measurement;
import net.cilib.problem.Problem;

/**
 *
 * @author gpampara
 */
public final class SimulationBuilder {

    private final SimulationScope scope;

    @Inject
    SimulationBuilder(SimulationScope scope) {
        this.scope = scope;
    }

    public PopulationBasedSimulationBuilder newPopulationBasedSimulation() {
        return new PopulationBasedSimulationBuilder(scope);
    }

    public static class PopulationBasedSimulationBuilder implements Simulation.Builder {

        private final SimulationScope scope;
        private PopulationBasedAlgorithm algorithm;
        private Problem problem;
        private List<Measurement> measurements;
        private Topology initialTopology;

        private PopulationBasedSimulationBuilder(SimulationScope scope) {
            this.scope = scope;
        }

        public PopulationBasedSimulationBuilder using(PopulationBasedAlgorithm alg) {
            this.algorithm = checkNotNull(alg, "Provided algorithm is null.");
            return this;
        }

        public PopulationBasedSimulationBuilder on(Problem problem) {
            this.problem = checkNotNull(problem, "Provided problem is null.");
            return this;
        }

        public PopulationBasedSimulationBuilder measuredBy(List<Measurement> measurements) {
            this.measurements = checkNotNull(measurements, "Provided measurements are null.");
            return this;
        }

        public PopulationBasedSimulationBuilder initialTopology(Topology topology) {
            this.initialTopology = checkNotNull(topology, "Provided initial topology is null.");
            return this;
        }

        @Override
        public Simulation build() {
            checkNotNull(algorithm);
            checkNotNull(problem);
            checkNotNull(initialTopology);
            checkNotNull(measurements);

            try {
                return new Simulation() {
                    final PopulationBasedAlgorithm localAlgorithm = algorithm;
                    final Topology localTopology = initialTopology;
                    final Problem localProblem = problem;
                    final List<Measurement> localMeasurements = measurements;

                    @Override
                    public void execute(List<Predicate<Algorithm>> stoppingConditions) {
                        checkNotNull(stoppingConditions);
                        checkArgument(stoppingConditions.size() >= 1, "At least 1 stopping condition is requried.");

                        scope.enter();
                        scope.seed(Key.get(Topology.class), localTopology); // Seed initial topology
                        scope.seed(Key.get(Problem.class), localProblem); // Seed the simulation problem

                        try {
                            Predicate<Algorithm> aggregate = Predicates.or(stoppingConditions);
                            Topology<Entity> current = (Topology<Entity>) localTopology; // Current topology is inital topology

                            while (aggregate.apply(localAlgorithm)) {
                                current = localAlgorithm.iterate(current);
                                scope.update(Key.get(Topology.class), current); // Reset the scoped instance
                            }
                        } finally {
                            scope.exit();
                        }
                    }
                };
            } finally {
                this.algorithm = null;
                this.initialTopology = null;
                this.measurements = null;
                this.problem = null;
            }
        }
    }
}
