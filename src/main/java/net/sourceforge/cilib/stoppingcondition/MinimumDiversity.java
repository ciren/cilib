package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.measurement.single.diversity.Diversity;
import net.sourceforge.cilib.type.types.Real;

/**
 * A stopping condition that is based on the {@link Diversity} of the population. The
 * {@link Algorithm} will stop as soon as the population's diversity drops below a (user-specified)
 * threshold for a (user-specified) number of consecutive iterations.
 * @author Theuns Cloete
 */
public class MinimumDiversity implements StoppingCondition {
	private static final long serialVersionUID = 8678755417913002799L;

	// minimum diversity value to satisfy the StoppingCondition
	private ControlParameter minimumDiversity;
	// number of consecutive iterations for which the diversity should be below minimumDiversity before stopping
	private ControlParameter consecutiveIterations;
	// Diversity object used to calculate diversity
	private Diversity diversity;
	// used to determine a rough completion percentage
	private double maximumDiversity = 0.0;
	// stores the last calculated diversity value
	private double calculatedDiversity = 0.0;
	// number of consecutive iterations for which the diversity has been below minimumDiversity
	private int iterations = 0;

	public MinimumDiversity() {
		minimumDiversity = new ConstantControlParameter(1.0);
		consecutiveIterations = new ConstantControlParameter(10);
		diversity = new Diversity();
	}

	public MinimumDiversity(MinimumDiversity rhs) {
		minimumDiversity = rhs.minimumDiversity.getClone();
		consecutiveIterations = rhs.consecutiveIterations.getClone();
		diversity = rhs.diversity.getClone();
		maximumDiversity = rhs.maximumDiversity;
		calculatedDiversity = rhs.calculatedDiversity;
		iterations = rhs.iterations;
	}

	public MinimumDiversity getClone() {
		return new MinimumDiversity(this);
	}

	/**
	 * Calculate a rough completion percentage estimate of the {@link Algorithm} based on the
	 * {@link #calculatedDiversity}, {@link #maximumDiversity} and {@link #minimumDiversity}. It is
	 * normal for the completion percentage to be <i>irregular</i> or <i>unpredictable</i>, because the
	 * {@link #calculatedDiversity} and {@link #maximumDiversity} will sporadically change over time.
	 * @return the completion percentage as a number between 0 and 1
	 */
	public double getPercentageCompleted() {
		return 1.0 - ((calculatedDiversity - minimumDiversity.getParameter()) / (maximumDiversity - minimumDiversity.getParameter()));
	}

	/**
	 * Calculate the diversity of the population and {@link #calculatedDiversity store} it. We also
	 * keep track of the {@link #maximumDiversity maximum diversity}. The number of consecutive
	 * {@link #iterations} are incremented when {@link #calculatedDiversity} <
	 * {@link #minimumDiversity} or reset to 0 otherwise.
	 * @return true when {@link #iterations} > {@link #consecutiveIterations}; false otherwise
	 */
	public boolean isCompleted() {
		updateControlParameters();
		calculatedDiversity = ((Real) diversity.getValue()).getReal();
		maximumDiversity = Math.max(maximumDiversity, calculatedDiversity);

		iterations = calculatedDiversity < minimumDiversity.getParameter() ? iterations + 1 : 0;

		return iterations >= consecutiveIterations.getParameter();
	}

	/**
	 * Update the {@link #minimumDiversity} and {@link #consecutiveIterations} {@link ControlParameter ControlParameters}.
	 */
	private void updateControlParameters() {
		minimumDiversity.updateParameter();
		consecutiveIterations.updateParameter();
	}

	/**
	 * Any {@link ControlParameter} can be used to control the {@link #minimumDiversity} value.
	 * @param md a {@link ControlParameter} to control the {@link #minimumDiversity} value
	 */
	public void setMinimumDiversity(ControlParameter md) {
		minimumDiversity = md;
	}

	/**
	 * Any {@link ControlParameter} can be used to control the {@link #consecutiveIterations} value.
	 * @param ci a {@link ControlParameter} to control the {@link #consecutiveIterations} value
	 */
	public void setConsecutiveIterations(ControlParameter ci) {
		consecutiveIterations = ci;
	}

	/**
	 * The manner in which the diversity should be calculated can be constructed using the
	 * {@link Diversity} hierarchy and its strategies.
	 * @param d a {@link Diversity} object that will be used to calculate the diversity of the
	 *        population
	 */
	public void setDiversity(Diversity d) {
		diversity = d;
	}

	/**
	 * Not used
	 */
	public void setAlgorithm(Algorithm a) {
	}
}
