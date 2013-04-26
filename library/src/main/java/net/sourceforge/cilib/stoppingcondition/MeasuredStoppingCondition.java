/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.type.types.Numeric;

/**
 * A stopping condition which uses measurements to determine if an algorithm has completed e.g.
 * MeasuredStoppingCondition(new Iterations(), new Maximum(), 1000) will stop an algorithm once the number of
 * iterations is greater than or equal to 1000, MeasuredStoppingCondition(new Diversity(), new Minimum(), 0.01)
 * stops it when the population's diversity is less than or equal to 0.01, etc.
 */
public class MeasuredStoppingCondition implements StoppingCondition<Algorithm> {
    
    private CompletionCalculator predicate;
    private double target;
    private Measurement<? extends Numeric> measurement;
    
    public MeasuredStoppingCondition() {
        this(new Iterations(), new Maximum(), 1000);
    }
    
    public MeasuredStoppingCondition(MeasuredStoppingCondition rhs) {
        this.measurement = rhs.measurement.getClone();
        this.predicate = rhs.predicate;
        this.target = rhs.target;
    }

    public MeasuredStoppingCondition(Measurement measurement, CompletionCalculator predicate, double target) {
        this.measurement = measurement;
        this.predicate = predicate;
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeasuredStoppingCondition getClone() {
        return new MeasuredStoppingCondition(this);
    }

    @Override
    public double getPercentageCompleted(Algorithm algorithm) {
        return predicate.getPercentage(measurement.getValue(algorithm).doubleValue(), target);
    }

    @Override
    public boolean apply(Algorithm algorithm) {
        return predicate.apply(measurement.getValue(algorithm).doubleValue(), target);
    }

    public double getTarget() {
        return target;
    }

    public CompletionCalculator getPredicate() {
        return predicate;
    }

    public Measurement<? extends Numeric> getMeasurement() {
        return measurement;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public void setPredicate(CompletionCalculator predicate) {
        this.predicate = predicate;
    }

    public void setMeasurement(Measurement<? extends Numeric> measurement) {
        this.measurement = measurement;
    }
}
