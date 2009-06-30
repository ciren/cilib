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
package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.SingularAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkRetrievalVisitor;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO: Complete this javadoc.
 */
public class ErrorDv implements Measurement {
    private static final long serialVersionUID = 3315947215103835233L;

    public ErrorDv() {
    }

    public ErrorDv(ErrorDv rhs) {
//        super(rhs);
        throw new UnsupportedOperationException("public ErrorDv(ErrorDv rhs)");
    }

    public ErrorDv getClone() {
        return new ErrorDv(this);
    }

    public ErrorDv(EvaluationMediator eval) {
    }

    public String getDomain() {
        return "T";
    }

    public Type getValue(Algorithm algorithm) {
        EvaluationMediator eval = null;

        if (algorithm instanceof SingularAlgorithm) {
             eval = (EvaluationMediator) algorithm;
        }
        else {
            OptimisationProblem optimisationProblem = algorithm.getOptimisationProblem();
            NeuralNetworkRetrievalVisitor visitor = new NeuralNetworkRetrievalVisitor();
            optimisationProblem.accept(visitor);
            eval = visitor.getMediator();
        }

        NNError[] errorDv = eval.getErrorDv();
        Vector err = new Vector();

        for (int i = 0; i < errorDv.length; i++){
            err.add(new Real(errorDv[i].getValue().doubleValue()));
        }

        return err;
    }
}
