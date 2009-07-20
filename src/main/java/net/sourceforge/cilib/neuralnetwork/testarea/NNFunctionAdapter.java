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
package net.sourceforge.cilib.neuralnetwork.testarea;

import net.sourceforge.cilib.functions.AbstractFunction;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors.WeightCountingVisitor;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO: Complete this javadoc.
 */
public class NNFunctionAdapter extends AbstractFunction<Type, Double> {
    private static final long serialVersionUID = -8189968864920232174L;

    private EvaluationMediator mediator;
    private String intialisationDomain;

    public NNFunctionAdapter() {
        intialisationDomain = "";
    }

    @Override
    public Double evaluate(Type in) {
        mediator.getTopology().setWeights((Vector) in);
        mediator.performLearning();
        return mediator.getErrorDt()[0].getValue();
    }

    public Double getMaximum() {
        // TODO Auto-generated method stub
        return null;
    }

    public Double getMinimum() {
        // TODO Auto-generated method stub
        return null;
    }

    public NNFunctionAdapter getClone() {
        return new NNFunctionAdapter();
    }

    public EvaluationMediator getMediator() {
        return mediator;
    }

    public void setMediator(EvaluationMediator mediator) {
        this.mediator = mediator;
        mediator.performInitialisation();

        WeightCountingVisitor visitor = new WeightCountingVisitor();
        ((GenericTopology)mediator.getTopology()).acceptVisitor(visitor);

        setDomain(this.intialisationDomain + "^" + visitor.getWeightCount());
    }

    public String getIntialisationDomain() {
        return intialisationDomain;
    }

    public void setIntialisationDomain(String intialisationDomain) {
        this.intialisationDomain = intialisationDomain;
    }

}
