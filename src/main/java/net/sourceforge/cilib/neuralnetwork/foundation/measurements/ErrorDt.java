/*
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
package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

public class ErrorDt implements Measurement {
	private static final long serialVersionUID = -5027085270461720189L;
	EvaluationMediator eval;

	public ErrorDt() {
		eval = null;
	}

	public ErrorDt(ErrorDt rhs) {
//		super(rhs);
		throw new UnsupportedOperationException("public ErrorDt(ErrorDt rhs)");
	}

	public ErrorDt getClone() {
		return new ErrorDt(this);
	}

	public ErrorDt(EvaluationMediator eval) {
		super();
		// TODO Auto-generated constructor stub
		this.eval = eval;
	}

	public String getDomain() {
		return "T";
	}

	public Type getValue() {
		NNError[] errorDt = eval.getErrorDt();
		Vector err = new Vector();
		
		for (int i = 0; i < errorDt.length; i++){
			err.add(new Real(errorDt[i].getValue().doubleValue()));
		}
		
		return err;
	}

	public void setEval(EvaluationMediator eval) {
		this.eval = eval;
	}
}
