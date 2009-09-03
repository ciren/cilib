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
package net.sourceforge.cilib.problem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author Edwin Peer
 *
 */
public class MOOptimisationProblem implements OptimisationProblem, List<OptimisationProblem> {

    private static final long serialVersionUID = 4997914969290350571L;
    protected List<OptimisationProblem> problems;

    public MOOptimisationProblem() {
        problems = new ArrayList<OptimisationProblem>();
    }

    public MOOptimisationProblem(MOOptimisationProblem copy) {
        this.problems = new ArrayList<OptimisationProblem>();
        for (OptimisationProblem optimisationProblem : copy.problems) {
            this.problems.add(optimisationProblem.getClone());
        }
    }

    @Override
    public MOOptimisationProblem getClone() {
        return new MOOptimisationProblem(this);
    }

    public MOFitness getFitness(Type[] solutions) {
        return new MOFitness(this, solutions);
    }

    @Override
    public MOFitness getFitness(Type solution) {
        return new MOFitness(this, solution);
    }

    public Fitness getFitness(int index, Type solution) {
        return problems.get(index).getFitness(solution);
    }

    @Override
    public int getFitnessEvaluations() {
        int sum = 0;
        for (OptimisationProblem problem : this.problems) {
            sum += problem.getFitnessEvaluations();
        }
        return sum;
    }

    @Override
    public DomainRegistry getDomain() {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    @Override
    public DataSetBuilder getDataSetBuilder() {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    @Override
    public void setDataSetBuilder(DataSetBuilder dataSetBuilder) {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    @Override
    public void accept(ProblemVisitor visitor) {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    @Override
    public void changeEnvironment() {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    @Override
    public boolean add(OptimisationProblem problem) {
        return this.problems.add(problem);
    }

    @Override
    public boolean addAll(Collection<? extends OptimisationProblem> problems) {
        return this.problems.addAll(problems);
    }

    @Override
    public void clear() {
        this.problems.clear();
    }

    @Override
    public boolean contains(Object object) {
        return this.problems.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        return this.problems.containsAll(objects);
    }

    @Override
    public boolean isEmpty() {
        return this.problems.isEmpty();
    }

    @Override
    public Iterator<OptimisationProblem> iterator() {
        return this.problems.iterator();
    }

    @Override
    public boolean remove(Object object) {
        return this.problems.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        return this.problems.removeAll(objects);
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return this.problems.retainAll(objects);
    }

    @Override
    public int size() {
        return this.problems.size();
    }

    @Override
    public Object[] toArray() {
        return this.problems.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.problems.toArray(a);
    }

    @Override
    public void add(int index, OptimisationProblem problem) {
        this.problems.add(index, problem);
    }

    @Override
    public boolean addAll(int index, Collection<? extends OptimisationProblem> problems) {
        return this.problems.addAll(problems);
    }

    @Override
    public OptimisationProblem get(int index) {
        return this.problems.get(index);
    }

    @Override
    public int indexOf(Object object) {
        return this.problems.indexOf(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return this.problems.lastIndexOf(object);
    }

    @Override
    public ListIterator<OptimisationProblem> listIterator() {
        return this.problems.listIterator();
    }

    @Override
    public ListIterator<OptimisationProblem> listIterator(int index) {
        return this.problems.listIterator(index);
    }

    @Override
    public OptimisationProblem remove(int index) {
        return this.problems.remove(index);
    }

    @Override
    public OptimisationProblem set(int index, OptimisationProblem problem) {
        return this.problems.set(index, problem);
    }

    @Override
    public List<OptimisationProblem> subList(int fromIndex, int toIndex) {
        return this.problems.subList(fromIndex, toIndex);
    }
}
