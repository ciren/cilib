/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.moo.archive.constrained;

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
public abstract class ConstrainedArchive extends Archive {

    private int capacity;

    public ConstrainedArchive() {
        this.capacity = 1000;
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
        if (this.dominates(candidateSolution).size() == 0) {

            // Remove all the solutions in the archive that is dominated by the candidate solution.
            removeAll(this.isDominatedBy(candidateSolution));

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
