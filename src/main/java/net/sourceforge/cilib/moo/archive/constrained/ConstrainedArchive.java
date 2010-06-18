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
package net.sourceforge.cilib.moo.archive.constrained;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ForwardingCollection;
import java.util.Collection;

import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * <p>
 * An {@link Archive} constrained by the number of solutions that it can store.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public abstract class ConstrainedArchive extends ForwardingCollection<OptimisationSolution> implements Archive {

    private Predicate<OptimisationSolution> predicate;
    private int capacity;

    public ConstrainedArchive() {
        this.predicate = Predicates.alwaysTrue();
        this.capacity = 100000;
    }

    public ConstrainedArchive(ConstrainedArchive copy) {
        this.predicate = copy.predicate;
        this.capacity = copy.capacity;
    }

    public void setPredicate(Predicate<OptimisationSolution> predicate) {
        this.predicate = predicate;
    }

    public Predicate<OptimisationSolution> getPredicate() {
        return this.predicate;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Iterates through the collection of {@code candidateSolutions} and adds the
     * non-dominated solutions to the archive. If the archive becomes too large
     * a pruning strategy is invoked afterwards.
     * @param candidateSolutions The solutions to add to the archive.
     * @return True if the archive changed as a result of the method call.
     */
    @Override
    public final boolean addAll(Collection<? extends OptimisationSolution> candidateSolutions) {
        // For each candidate solution, try to add it to the archive.
        boolean changed = false;
        for (OptimisationSolution candidateSolution : candidateSolutions) {
            changed |= addNonDominatedSolution(candidateSolution);
        }

        // If the archive changed and the size of the archive is too large then invoke pruning method.
        if (changed && size() > getCapacity()) {
            prune();
        }

        return changed;
    }

    /**
     * Adds the {@code candidateSolution} if no solution within the archive dominates it.
     * If the archive becomes too large a pruning strategy is invoked afterwards.
     * @param candidateSolution The solution to add to the archive.
     * @return True if the archive changed as a result if the method call.
     */
    @Override
    public final boolean add(OptimisationSolution candidateSolution) {
        boolean changed = addNonDominatedSolution(candidateSolution);

        // If the archive changed and the size of the archive is too large then invoke pruning method.
        if (changed && size() > getCapacity()) {
            prune();
        }

        return changed;
    }

    protected final boolean addNonDominatedSolution(OptimisationSolution candidateSolution) {
        // If no solution in the archive dominates the candidate solution then proceed...
        if (this.predicate.apply(candidateSolution) && !this.dominates(candidateSolution)) {

            // Remove all the solutions in the archive that is dominated by the candidate solution.
            removeAll(getDominated(candidateSolution));

            // Add the candidate solution to the archive.
            return addToStructure(candidateSolution);
        }

        return false;
    }

    /**
     * Adds a non-dominated {@code candidateSolution} into the archive.
     * @param candidateSolution The non-dominated solution to add to the archive.
     */
    protected abstract boolean addToStructure(OptimisationSolution candidateSolution);

    /**
     * This method needs to be implemented by all subclasses to provide a way
     * in which certain solutions will be removed if the archive grows too big.
     */
    protected abstract void prune();
}
