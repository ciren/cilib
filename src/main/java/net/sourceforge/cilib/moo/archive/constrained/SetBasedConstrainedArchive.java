/**
 * Copyright (C) 2003 - 2008
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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.util.selection.selectionstrategies.ProbabilisticSelectionStrategy;
import net.sourceforge.cilib.util.selection.selectionstrategies.SelectionStrategy;

/**
 * <p>
 * A constrained set-driven {@link Archive} implementation. It makes use of
 * a {@link SelectionStrategy} to determine which solution from the archive
 * will be selected next for removal.
 * </p>
 * 
 * @author Wiehann Matthysen
 */
public class SetBasedConstrainedArchive extends ConstrainedArchive {

    private Set<OptimisationSolution> solutions;
    private SelectionStrategy<OptimisationSolution> deleteSelectionStrategy;

    public SetBasedConstrainedArchive() {
        this.solutions = new LinkedHashSet<OptimisationSolution>();
        this.deleteSelectionStrategy = new ProbabilisticSelectionStrategy<OptimisationSolution>();
    }

    public void setDeleteSelectionStrategy(SelectionStrategy<OptimisationSolution> deleteSelectionStrategy) {
        this.deleteSelectionStrategy = deleteSelectionStrategy;
    }

    public SelectionStrategy<OptimisationSolution> getDeleteSelectionStrategy() {
        return this.deleteSelectionStrategy;
    }

    @Override
    public Collection<OptimisationSolution> dominates(OptimisationSolution candidateSolution) {
        List<OptimisationSolution> dominantSolutions = new LinkedList<OptimisationSolution>();
        for (OptimisationSolution archiveSolution : this.solutions) {
            if (archiveSolution.compareTo(candidateSolution) > 0) {
                dominantSolutions.add(archiveSolution);
            }
        }
        return dominantSolutions;
    }

    @Override
    public Collection<OptimisationSolution> isDominatedBy(OptimisationSolution candidateSolution) {
        List<OptimisationSolution> dominatedSolutions = new LinkedList<OptimisationSolution>();
        for (OptimisationSolution archiveSolution : this.solutions) {
            if (candidateSolution.compareTo(archiveSolution) > 0) {
                dominatedSolutions.add(archiveSolution);
            }
        }
        return dominatedSolutions;
    }

    @Override
    protected void prune() {
        // If the archive size is greater than the capacity, select a group of solutions and remove them from the archive.
        int numSolutionsToRemove = size() - getCapacity();
        Collection<OptimisationSolution> solutionsToRemove = this.deleteSelectionStrategy.select(this.solutions, numSolutionsToRemove);
        removeAll(solutionsToRemove);
    }

    @Override
    public boolean add(OptimisationSolution optimisationSolution) {
        return this.solutions.add(optimisationSolution);
    }

    @Override
    public boolean addAll(Collection<? extends OptimisationSolution> optimisationSolutions) {
        return this.solutions.addAll(optimisationSolutions);
    }

    @Override
    public void clear() {
        this.solutions.clear();
    }

    @Override
    public boolean contains(Object object) {
        return this.solutions.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        return this.solutions.containsAll(objects);
    }

    @Override
    public boolean isEmpty() {
        return this.solutions.isEmpty();
    }

    @Override
    public Iterator<OptimisationSolution> iterator() {
        return this.solutions.iterator();
    }

    @Override
    public boolean remove(Object object) {
        return this.solutions.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        return this.solutions.removeAll(objects);
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return this.solutions.retainAll(objects);
    }

    @Override
    public int size() {
        return this.solutions.size();
    }

    @Override
    public Object[] toArray() {
        return this.solutions.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.solutions.toArray(a);
    }
}
