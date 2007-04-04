package net.sourceforge.cilib.util;

/**
 * Manhattan Distance is a special case of the {@link net.sourceforge.cilib.util.MinkowskiMetric Minkowski Metric} with 'alpha' := 1.
 * @author Theuns Cloete
 */
public class ManhattanDistanceMeasure extends MinkowskiMetric {
	public ManhattanDistanceMeasure() {
		super(1);
	}
}
