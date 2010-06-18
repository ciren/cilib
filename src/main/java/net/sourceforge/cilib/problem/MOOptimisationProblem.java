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
package net.sourceforge.cilib.problem;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;
import java.util.List;

import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author Edwin Peer
 *
 */
public class MOOptimisationProblem extends ForwardingList<OptimisationProblem> implements OptimisationProblem {

    private static final long serialVersionUID = 4997914969290350571L;
    protected final List<OptimisationProblem> problems;

    public MOOptimisationProblem() {
        this.problems = Lists.newArrayList();
    }

    public MOOptimisationProblem(MOOptimisationProblem copy) {
        this.problems = Lists.newArrayList();
        for (OptimisationProblem optimisationProblem : copy.problems) {
            this.problems.add(optimisationProblem.getClone());
        }
    }

    @Override
    public MOOptimisationProblem getClone() {
        return new MOOptimisationProblem(this);
    }

    public MOFitness getFitness(Type[] solutions) {
        return Fitnesses.create(this, solutions);
    }

    @Override
    public MOFitness getFitness(Type solution) {
        return Fitnesses.create(this, solution);
    }

    public Fitness getFitness(int index, Type solution) {
        return this.problems.get(index).getFitness(solution);
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
        visitor.visit(this);
    }

    @Override
    public void changeEnvironment() {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    @Override
    protected List<OptimisationProblem> delegate() {
        return this.problems;
    }
}
