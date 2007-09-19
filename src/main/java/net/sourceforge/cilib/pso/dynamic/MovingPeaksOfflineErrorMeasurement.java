/**
 * 
 */
package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.continuous.dynamic.MovingPeaks;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.MixedVector;

/**
 * @author anna
 *
 */
public class MovingPeaksOfflineErrorMeasurement implements Measurement {

	private static final long serialVersionUID = 2632671785674388015L;

	public MovingPeaksOfflineErrorMeasurement() {}
	public MovingPeaksOfflineErrorMeasurement(MovingPeaksOfflineErrorMeasurement rhs) {}
	
	public MovingPeaksOfflineErrorMeasurement clone() {
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
		MovingPeaks func = (MovingPeaks)((FunctionMaximisationProblem)(Algorithm.get().getOptimisationProblem())).getFunction();
		MixedVector err = new MixedVector();
		err.add(new Real(func.get_offline_error()));
		return err;
	}
}
