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
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * TODO: Complete this javadoc.
 */
public class RobelOverfittingRho implements Measurement {
    private static final long serialVersionUID = -3535954032635090424L;

    public RobelOverfittingRho() {
    }

    public RobelOverfittingRho(RobelOverfittingRho rhs) {
//        super(rhs);
        throw new UnsupportedOperationException("public RobelOverfittingRho(RobelOverfittingRho rhs)");
    }

    public RobelOverfittingRho getClone() {
        return new RobelOverfittingRho(this);
    }

    public String getDomain() {
        return "R";
    }

    public Type getValue(Algorithm algorithm) {
        NeuralNetworkController controller = (NeuralNetworkController) algorithm;
        NNError[] errorDt = ((NeuralNetworkProblem) controller.getOptimisationProblem()).getEvaluationStrategy().getErrorDt();
        NNError[] errorDg = ((NeuralNetworkProblem) controller.getOptimisationProblem()).getEvaluationStrategy().getErrorDg();

        Real rho = new Real();
        rho.setReal(errorDg[0].getValue().doubleValue() / errorDt[0].getValue().doubleValue());
        return rho;
    }
}
