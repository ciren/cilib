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
package net.sourceforge.cilib.neuralnetwork.foundation;

import net.sourceforge.cilib.functions.AbstractFunction;
import net.sourceforge.cilib.neuralnetwork.testarea.NNFunctionAdapter;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.ProblemVisitor;

public class NeuralNetworkRetrievalVisitor extends ProblemVisitor {

    private EvaluationMediator mediator;

    @Override
    public void visit(Problem o) {
        FunctionOptimisationProblem functionOptimisationProblem = (FunctionOptimisationProblem) o;
        AbstractFunction function = (AbstractFunction) functionOptimisationProblem.getFunction();
        NNFunctionAdapter nnFunctionAdapter = (NNFunctionAdapter) function;

        this.mediator = nnFunctionAdapter.getMediator();
    }

    public EvaluationMediator getMediator() {
        return mediator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return false;
    }
}
