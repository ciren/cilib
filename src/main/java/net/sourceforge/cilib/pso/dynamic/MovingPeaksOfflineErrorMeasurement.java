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
package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.continuous.dynamic.MovingPeaks;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author anna
 *
 */
public class MovingPeaksOfflineErrorMeasurement implements Measurement {

	private static final long serialVersionUID = 2632671785674388015L;

	public MovingPeaksOfflineErrorMeasurement() {}
	public MovingPeaksOfflineErrorMeasurement(MovingPeaksOfflineErrorMeasurement rhs) {}
	
	public MovingPeaksOfflineErrorMeasurement getClone() {
		return new MovingPeaksOfflineErrorMeasurement(this);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.measurement.Measurement#getDomain()
	 */
	public String getDomain() {
		return "R";
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.measurement.Measurement#getValue()
	 */
	public Type getValue() {
		MovingPeaks func = (MovingPeaks) ((FunctionMaximisationProblem) (Algorithm.get().getOptimisationProblem())).getFunction();
		Vector err = new Vector();
		err.add(new Real(func.getOfflineError()));
		return err;
	}
}
