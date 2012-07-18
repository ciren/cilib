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
public class QuadTree implements Archive {

    @Override
    public boolean dominates(OptimisationSolution candidateSolution) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDominatedBy(OptimisationSolution candidateSolution) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<OptimisationSolution> getDominant(OptimisationSolution candidateSolution) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<OptimisationSolution> getDominated(OptimisationSolution candidateSolution) {
        throw new UnsupportedOperationException("Not supported yet.");
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
