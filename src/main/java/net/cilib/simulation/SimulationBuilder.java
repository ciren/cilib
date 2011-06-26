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
package net.cilib.simulation;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.inject.Inject;
import java.util.List;
import net.cilib.algorithm.PopulationBasedAlgorithm;
import net.cilib.collection.Topology;
import net.cilib.inject.SimulationScope;
import net.cilib.measurement.Measurement;
import net.cilib.problem.Problem;

/**
 *
 * @author gpampara
 */
public final class SimulationBuilder {

//    private final SimulationScope scope;
//
//    @Inject
//    SimulationBuilder(SimulationScope scope) {
//        this.scope = scope;
//    }
//
//    public PopulationBasedSimulationBuilder newPopulationBasedSimulation() {
//        return new PopulationBasedSimulationBuilder(scope);
//    }
//
//    public static class PopulationBasedSimulationBuilder implements Simulation.Builder {
//
//        private final SimulationScope scope;
//        private PopulationBasedAlgorithm algorithm;
//        private Problem problem;
//        private List<Measurement> measurements;
//        private Topology initialTopology;
//
//        private PopulationBasedSimulationBuilder(SimulationScope scope) {
//            this.scope = scope;
//        }
//
//        public PopulationBasedSimulationBuilder using(PopulationBasedAlgorithm alg) {
//            this.algorithm = checkNotNull(alg, "Provided algorithm is null.");
//            return this;
//        }
//
//        public PopulationBasedSimulationBuilder on(Problem problem) {
//            this.problem = checkNotNull(problem, "Provided problem is null.");
//            return this;
//        }
//
//        public PopulationBasedSimulationBuilder measuredBy(List<Measurement> measurements) {
//            this.measurements = checkNotNull(measurements, "Provided measurements are null.");
//            return this;
//        }
//
//        public PopulationBasedSimulationBuilder initialTopology(Topology topology) {
//            this.initialTopology = checkNotNull(topology, "Provided initial topology is null.");
//            return this;
//        }
//
//        @Override
//        public Simulation build() {
//            throw new UnsupportedOperationException();
////            checkNotNull(algorithm);
////            checkNotNull(problem);
////            checkNotNull(initialTopology);
////            checkNotNull(measurements);
////
////            try {
////                return new Simulation() {
////                    final PopulationBasedAlgorithm<Entity> localAlgorithm = algorithm;
////                    final Topology localTopology = initialTopology;
////                    final Problem localProblem = problem;
////                    final List<Measurement> localMeasurements = measurements;
////
////                    @Override
////                    public void execute(List<Predicate<Algorithm>> stoppingConditions) {
////                        checkNotNull(stoppingConditions);
////                        checkArgument(stoppingConditions.size() >= 1, "At least 1 stopping condition is requried.");
////
////                        scope.enter();
////                        scope.seed(Key.get(Topology.class), localTopology); // Seed initial topology
////                        scope.seed(Key.get(Problem.class), localProblem); // Seed the simulation problem
////
////                        try {
////                            Predicate<Algorithm> aggregate = Predicates.or(stoppingConditions);
////                            Topology<Entity> current = (Topology<Entity>) localTopology; // Current topology is initial topology
////
////                            while (aggregate.f(localAlgorithm)) {
////                                current = localAlgorithm.next(current);
////                                scope.update(Key.get(Topology.class), current); // Reset the scoped instance
////                            }
////                        } finally {
////                            scope.exit();
////                        }
////                    }
////                };
////            } finally {
////                this.algorithm = null;
////                this.initialTopology = null;
////                this.measurements = null;
////                this.problem = null;
////            }
//        }
//    }
}
