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
package net.sourceforge.cilib.moo.archive.unconstrained;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * Placeholder class for anybody interrested in implementing this datastructure.
 * The reference where the implementation details can be found is:
 *
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li> S. Mostaghim, J. Teich and A. Tyagi, "Comparison of Data Structures for Storing Pareto-sets in MOEA's", in
 * Proceedings of the IEEE World Congress on Computational Intelligence, vol 1, pp. 843-849, May 2002.
 * </li>
 * </ul>
 * </p>
 */
public class QuadTree extends Archive {

    @Override
    public Collection<OptimisationSolution> dominates(
            OptimisationSolution candidateSolution) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<OptimisationSolution> isDominatedBy(
            OptimisationSolution candidateSolution) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean add(OptimisationSolution e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends OptimisationSolution> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator<OptimisationSolution> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }
}
