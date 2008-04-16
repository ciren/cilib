/*
 * NNFunctionAdapter.java
 *
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
package net.sourceforge.cilib.neuralnetwork.testarea;

import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO: Complete this javadoc.
 */
public class NNFunctionAdapter extends Function {
	private static final long serialVersionUID = -8189968864920232174L;
	
	private EvaluationMediator mediator;

	@Override
	public Double evaluate(Object in) {
		mediator.getTopology().setWeights((Vector) in);
		mediator.performLearning();
		return mediator.getErrorDt()[0].getValue();
	}

	public Object getMaximum() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getMinimum() {
		// TODO Auto-generated method stub
		return null;
	}

	public NNFunctionAdapter getClone() {
		return new NNFunctionAdapter();
	}

}
