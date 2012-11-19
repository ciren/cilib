/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning;

import fj.F;
import static fj.data.List.*;
import fj.data.Stream;
import static fj.data.Stream.*;
import static fj.function.Doubles.*;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.AbstractProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.tuning.problem.ProblemListProvider;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

public class TuningProblem extends AbstractProblem {
    
    private Stream<Problem> problems;
    private Problem currentProblem;
    
    private AbstractAlgorithm targetAlgorithm;
    private ProblemListProvider problemsProvider;
    private Measurement<Real> measurement;
    private int samples;
    
    public TuningProblem() {
        this.measurement = new net.sourceforge.cilib.measurement.single.Fitness();
        this.samples = 1;
    }

    @Override
    public AbstractProblem getClone() {
        return this;
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
    
    public void generateProblems() {
        problems = cycle(iterableStream(problemsProvider._1()));
        nextProblem();
    }
    
    public void nextProblem() {
        currentProblem = problems.head();
        problems = problems.tail()._1();
    }
    
    public void setMeasurement(Measurement<Real> measurement) {
        this.measurement = measurement;
    }

    public Measurement<Real> getMeasurement() {
        return measurement;
    }
    
    public void setProblemsProvider(ProblemListProvider problemProvider) {
        this.problemsProvider = problemProvider;
    }
    
    public ProblemListProvider getProblemsProvider() {
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
}
