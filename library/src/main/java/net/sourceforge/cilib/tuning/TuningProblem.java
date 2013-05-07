/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning;

import fj.F;
import static fj.data.List.range;
import static fj.function.Doubles.sum;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.*;
import net.sourceforge.cilib.problem.objective.Objective;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.tuning.problem.ProblemGenerator;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

public class TuningProblem extends AbstractProblem {
    
    private Problem currentProblem;    
    private AbstractAlgorithm targetAlgorithm;
    private ProblemGenerator problemsProvider;
    private Measurement<Real> measurement;
    private int samples;
    
    public TuningProblem() {
        this.measurement = new net.sourceforge.cilib.measurement.single.Fitness();
        this.samples = 1;
    }
    
    public TuningProblem(TuningProblem copy) {
        this.measurement = copy.measurement.getClone();
        this.samples = copy.samples;
        this.targetAlgorithm = copy.targetAlgorithm.getClone();
        this.problemsProvider = copy.problemsProvider;
    }

    @Override
    public TuningProblem getClone() {
        return new TuningProblem(this);
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        double f = sum(range(0, samples).map(new F<Integer, Double>(){
            @Override
            public Double f(Integer a) {
                targetAlgorithm.setOptimisationProblem(currentProblem);
                targetAlgorithm.performInitialisation();
                targetAlgorithm.runAlgorithm();
                return measurement.getValue(targetAlgorithm).doubleValue();
            }                    
        })) / samples;

        return objective.evaluate(f);
    }

    public void nextProblem() {
        currentProblem = problemsProvider._1();
    }
    
    public void setMeasurement(Measurement<Real> measurement) {
        this.measurement = measurement;
    }

    public Measurement<Real> getMeasurement() {
        return measurement;
    }
    
    public void setProblemsProvider(ProblemGenerator problemProvider) {
        this.problemsProvider = problemProvider;
    }
    
    public ProblemGenerator getProblemsProvider() {
        return problemsProvider;
    }
    
    public void setTargetAlgorithm(AbstractAlgorithm targetAlgorithm) {
        this.targetAlgorithm = targetAlgorithm;
    }

    public AbstractAlgorithm getTargetAlgorithm() {
        return targetAlgorithm;
    }
    
    public void setSamples(int samples) {
        this.samples = samples;
    }

    public int getSamples() {
        return samples;
    }

    public Problem getCurrentProblem() {
        return currentProblem;
    }

    @Override
    public Objective getObjective() {
        return ((AbstractProblem) currentProblem).getObjective();
    }
}
