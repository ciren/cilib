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
package net.cilib.entity;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.util.Comparator;
import static net.cilib.entity.EntityComparators.FITNESS_COMPARATOR;

/**
 * Basic individual class.
 * <p>
 * The individual is an {@link Entity} instances that maintains a single
 * {@linkplain CandidateSolution solution} and {@linkplain Fitness fitness}.
 *
 * @since 0.8
 * @author gpampara
 */
public final class Individual implements Entity {

    private final CandidateSolution solution;
    private final Fitness fitness;

    @Inject
    public Individual(@Assisted CandidateSolution solution, @Assisted Fitness fitness) {
        this.solution = solution;
        this.fitness = fitness;
    }

    @Override
    public PartialEntity plus(HasCandidateSolution that) {
        return newPartialIndividual(this).plus(that);
    }

    @Override
    public PartialEntity plus(PartialEntity that) {
        return newPartialIndividual(this).plus(that.build());
    }

    @Override
    public PartialEntity subtract(HasCandidateSolution that) {
        return newPartialIndividual(this).subtract(that);
    }

    @Override
    public PartialEntity subtract(PartialEntity that) {
        return newPartialIndividual(this).subtract(that.build());
    }

    @Override
    public PartialEntity multiply(double scalar) {
        return newPartialIndividual(this).multiply(scalar);
    }

    @Override
    public PartialEntity divide(double scalar) {
        return newPartialIndividual(this).divide(scalar);
    }

    @Override
    public CandidateSolution solution() {
        return CandidateSolution.copyOf(solution);
    }

    @Override
    public int size() {
        return this.solution.size();
    }

    @Override
    public Fitness fitness() {
        return fitness;
    }

    @Override
    public boolean equiv(Entity that) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity moreFit(Entity that) {
        return moreFit(that, FITNESS_COMPARATOR);
    }

    @Override
    public Entity moreFit(Entity that, Comparator<? super Entity> comparator) {
        return (comparator.compare(this, that) >= 0) ? this : that;
    }

    @Override
    public Entity lessFit(Entity that) {
        return lessFit(that, FITNESS_COMPARATOR);
    }

    @Override
    public Entity lessFit(Entity that, Comparator<? super Entity> comparator) {
        return (comparator.compare(this, that) < 0) ? this : that;
    }

//    @Override
//    public boolean isMoreFit(Entity than) {
//        return moreFit(than) == than;
//    }
//
//    @Override
//    public boolean isLessFit(Entity than) {
//        return lessFit(than) == than;
//    }
    @Override
    public int compareTo(Entity o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private PartialEntity newPartialIndividual(Individual individual) {
        return new PartialIndividual(individual);
    }

    private static class PartialIndividual implements Entity.PartialEntity {

        private double[] internalSolution;

        private PartialIndividual(Individual individual) {
            this.internalSolution = individual.solution.toArray();
        }

        // DI Here? Should the EntityFactory be injected?
        @Override
        public final Individual build() {
            return new Individual(CandidateSolution.copyOf(internalSolution), Fitnesses.inferior());
        }

        @Override
        public PartialEntity plus(HasCandidateSolution that) {
            final double[] thatSolution = that.solution().toArray();
            Preconditions.checkState(internalSolution.length == thatSolution.length);
            for (int i = 0, n = thatSolution.length; i < n; i++) {
                internalSolution[i] += thatSolution[i];
            }
            return this;
        }

        @Override
        public PartialEntity subtract(HasCandidateSolution that) {
            final double[] thatSolution = that.solution().toArray();
            Preconditions.checkState(internalSolution.length == thatSolution.length);
            for (int i = 0, n = internalSolution.length; i < n; i++) {
                internalSolution[i] -= thatSolution[i];
            }
            return this;
        }

        @Override
        public PartialEntity multiply(final double scalar) {
            for (int i = 0, n = internalSolution.length; i < n; i++) {
                internalSolution[i] *= scalar;
            }
            return this;
        }

        @Override
        public PartialEntity divide(double scalar) {
            Preconditions.checkArgument(Doubles.compare(scalar, 0.0) != 0, "Cannot divide with a 0.0!");
            for (int i = 0, n = internalSolution.length; i < n; i++) {
                internalSolution[i] /= scalar;
            }
            return this;
        }
    }
}
