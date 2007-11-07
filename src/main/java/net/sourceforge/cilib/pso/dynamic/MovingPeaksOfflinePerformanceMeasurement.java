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
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author anna
 *
 */
public class MovingPeaksOfflinePerformanceMeasurement implements Measurement {

	private static final long serialVersionUID = 3204341758731244688L;

	public MovingPeaksOfflinePerformanceMeasurement() {}
	public MovingPeaksOfflinePerformanceMeasurement(MovingPeaksOfflinePerformanceMeasurement rhs) {}
	
	public MovingPeaksOfflinePerformanceMeasurement clone() {
		return new MovingPeaksOfflinePerformanceMeasurement(this);
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
		Vector err = new Vector();
		err.add(new Real(func.get_offline_performance()));
		return err;
	}
}
