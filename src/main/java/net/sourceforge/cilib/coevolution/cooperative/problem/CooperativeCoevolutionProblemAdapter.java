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
/**
 *
 */
package net.sourceforge.cilib.coevolution.cooperative.problem;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Types;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This {@linkplain OptimisationProblem} is used by the participants of a {@linkplain CooperativeCoevolutionAlgorithm}. It stores the {@linkplain DimensionAllocation} of the participat, and provides the means to calculate
 * the fitness of {@linkplain Entitie}s of the participating {@linkplain Algorithm}s.
 *
 * @author leo
 * @author Theuns Cloete
 */
public class CooperativeCoevolutionProblemAdapter extends
        OptimisationProblemAdapter {
    private static final long serialVersionUID = 3764040830993620887L;
    private OptimisationProblem problem;
    private DomainRegistry problemDomain;
    private Vector context;
    private DimensionAllocation problemAllocation;

    /**
     * Creates an CooperativeCoevolutionProblemAdapter, which is assigned to each participant in\
     * a {@linkplain CooperativeCoevolutionAlgorithm}.
     * @param problem The original problem that is being optimized
     * @param problemAllocation The {@linkplain DimensionAllocation} which dictates how the solutions
     *     of the {@linkplain Entity}'s that are optimizing this problem fits into the original problem.
     * @param context The current context soltution of the {@linkplain CooperativeCoevolutionAlgorithm}
     */
    public CooperativeCoevolutionProblemAdapter(OptimisationProblem problem, DimensionAllocation problemAllocation, Vector context) {
        this.problem = problem;
        this.problemAllocation = problemAllocation;
        this.context = context.getClone();
        problemDomain = new StringBasedDomainRegistry();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < problemAllocation.getSize(); i++) {
            String tmp = Types.getRepresentation(context.get(problemAllocation.getProblemIndex(i)));
            builder.append(tmp);
            if (i < problemAllocation.getSize() - 1) {
                builder.append(",");
            }
        }

        problemDomain.setDomainString(builder.toString());
    }

    /**
     * @param copy
     */
    public CooperativeCoevolutionProblemAdapter(CooperativeCoevolutionProblemAdapter copy) {
        super(copy);
        this.context = copy.context.getClone();
        this.problem = copy.problem.getClone();
        this.problemAllocation = copy.problemAllocation.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        for (int i = 0; i < problemAllocation.getSize(); ++i) {
            context.set(problemAllocation.getProblemIndex(i), ((Vector) solution).get(i));
        }
        return problem.getFitness(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationProblemAdapter getClone() {
        return new CooperativeCoevolutionProblemAdapter(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainRegistry getDomain() {
        return problemDomain;
    }

    /**
     * @return Get the current {@linkplain DimensionAllocation}.
     */
    public DimensionAllocation getProblemAllocation() {
        return problemAllocation;
    }

    /**
     * Update the context vector with the given parameter.
     * @param context The new context.
     */
    public void updateContext(Vector context) {
        this.context = Vector.copyOf(context);
    }
}
