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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.recipes.RandomSelection;
import net.sourceforge.cilib.util.selection.recipes.SelectionRecipe;

/**
 * <p>
 * A constrained set-driven {@link Archive} implementation. It makes use of
 * a {@link Selection} to determine which solution from the archive
 * will be selected next for removal if the archive grows larger than the capacity.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class SetBasedConstrainedArchive extends ConstrainedArchive {

    private Set<OptimisationSolution> solutions;
    private SelectionRecipe<OptimisationSolution> pruningSelection;

    public SetBasedConstrainedArchive() {
        this.solutions = new LinkedHashSet<OptimisationSolution>();
        this.pruningSelection = new RandomSelection<OptimisationSolution>();
    }

    public void setPruningSelection(SelectionRecipe<OptimisationSolution> pruningSelection) {
        this.pruningSelection = pruningSelection;
    }

    public SelectionRecipe<OptimisationSolution> getPruningSelection() {
        return this.pruningSelection;
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
        for (int i = 0; i < numSolutionsToRemove; ++i) {
            OptimisationSolution solutionToRemove = this.pruningSelection.select(this);
            remove(solutionToRemove);
        }
    }

    @Override
    public boolean addToStructure(OptimisationSolution optimisationSolution) {
        return this.solutions.add(optimisationSolution);
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

    @Override
    public boolean addAll(int index, Collection<? extends OptimisationSolution> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OptimisationSolution get(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OptimisationSolution set(int index, OptimisationSolution element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void add(int index, OptimisationSolution element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OptimisationSolution remove(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<OptimisationSolution> listIterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<OptimisationSolution> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<OptimisationSolution> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
