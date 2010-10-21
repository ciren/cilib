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
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Factory class to create {@code Individual} instances, given a
 * {@code CandidateSolution}. The returned instances will be created and the
 * {@code Fitness} of the provided solution will also have been determined.
 *
 * @author gpampara
 */
public final class IndividualProvider implements Provider<Individual> {

    private final FitnessProvider fitnessProvider;
    private CandidateSolution solution;

    @Inject
    public IndividualProvider(FitnessProvider fitnessProvider) {
        this.fitnessProvider = fitnessProvider;
    }

    /**
     * The {@code CandidateSolution} to use for the creation of an
     * {@code Individual}. A copy of the provided {@code CandidateSolution} is
     * then made to prevent any potential state problems.
     * @param solution the {@code CandidateSolution}.
     * @return the current {@code IndividualProvider}.
     */
    public IndividualProvider solution(CandidateSolution solution) {
        this.solution = CandidateSolution.copyOf(solution);
        return this;
    }

    /**
     * Create the {@code Individual} instance which is fully populated.
     * If a solution has not been defined, a {@link IllegalStateException}
     * will be raised.
     * @return a fully complete {@code Individual}.
     * @throws IllegalStateException if the provider is in an inconsistent
     *         state.
     */
    @Override
    public Individual get() {
        Preconditions.checkState(this.solution != null,
            "Provide a candidate solution to create an Individual.");

        try {
            return new Individual(solution, fitnessProvider.finalize(solution));
        } finally {
            this.solution = null;
        }
    }
}
