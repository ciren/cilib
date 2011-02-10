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
package net.sourceforge.cilib.moo.archive;

import java.util.Collection;

import net.sourceforge.cilib.moo.archive.constrained.SetBasedConstrainedArchive;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * <p>
 * A representation of an archive of non-dominated solutions used by Multi-objective optimisation
 * algorithms for solution storage during a search.
 * </p>
 *
 * @author Andries Engelbrecht
 * @author Wiehann Matthysen
 */
public interface Archive extends Collection<OptimisationSolution> {

    public static class Provider {

        private static ThreadLocal<Archive> currentArchive = new ThreadLocal<Archive>() {

            @Override
            protected Archive initialValue() {
                return new SetBasedConstrainedArchive();
            }
        };

        private Provider() {
        }

        /**
         * Static entrypoint to get to the Archive. This method is usefull especially
         * in situations where you need to get to the archive from different locations
         * such as a {@code GuideSelectionStrategy}, or the {@link ArchivingIterationStep}
         * at the end of a multi-objective algorithm.
         * @return The currently active archive for a multi-objective algortihm.
         */
        public static Archive get() {
            return currentArchive.get();
        }

        /***
         * Set the archive to be used by a multi-objective optimisation algorithm.
         * @param archive A concrete archive implementation to be used as the active archive.
         */
        public static void set(Archive archive) {
            currentArchive.set(archive);
        }

        public static void remove() {
            currentArchive.remove();
        }
    }

    /**
     * Checks the entire archive and determines whether a solution exists that dominates {@code candidateSolution}.
     * @param candidateSolution The solution to compare against all of the solutions in the archive.
     * @return True if a solution exists that dominates {@code candidateSolution}, false otherwise.
     */
    public abstract boolean dominates(OptimisationSolution candidateSolution);

    /**
     * Checks the entire archive and determines whether a solution exists that is dominated by {@code candidateSolution}.
     * @param candidateSolution The solution to compare against all of the solutions in the archive.
     * @return True if a solution exists that is dominated by {@code candidateSolution}, false otherwise.
     */
    public abstract boolean isDominatedBy(OptimisationSolution candidateSolution);

    /**
     * Checks the entire archive and accumulates the solutions that dominates {@code candidateSolution}.
     * @param candidateSolution The solution to compare against all of the solutions in the archive.
     * @return The collection of solutions that dominates {@code candidateSolution}.
     */
    public abstract Collection<OptimisationSolution> getDominant(OptimisationSolution candidateSolution);

    /**
     * Checks the archive and accumulates all solutions that is dominated by {@code candidateSolution}.
     * @param candidateSolution The solution to compare against all of the solutions in the archive.
     * @return The collection of solutions dominated by the {@code candidateSolution}.
     */
    public abstract Collection<OptimisationSolution> getDominated(OptimisationSolution candidateSolution);
}
