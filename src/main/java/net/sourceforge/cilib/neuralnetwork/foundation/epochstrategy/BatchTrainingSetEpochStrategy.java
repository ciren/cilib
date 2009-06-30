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
package net.sourceforge.cilib.neuralnetwork.foundation.epochstrategy;

import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.type.types.container.TypeList;

public class BatchTrainingSetEpochStrategy implements EpochStrategy {

    @Override
    public void performIteration(EvaluationMediator evaluationMediator) {
        evaluationMediator.resetError(evaluationMediator.getErrorDt());
        evaluationMediator.setErrorNoPatterns(evaluationMediator.getErrorDt(), evaluationMediator.getData().getTrainingSetSize());

        NeuralNetworkDataIterator iteratorDt = evaluationMediator.getData().getTrainingSetIterator();

        //iterate over each applicable pattern in training dataset
        while (iteratorDt.hasMore()){

            TypeList output = evaluationMediator.getTopology().evaluate(iteratorDt.value());
            evaluationMediator.incrementEvaluationsPerEpoch();

            //compute the per pattern error.
            evaluationMediator.computeErrorIteration(evaluationMediator.getErrorDt(), output, iteratorDt.value());

            iteratorDt.next();
        }

        //finalise errors
        evaluationMediator.finaliseErrors(evaluationMediator.getErrorDt());
    }

}
