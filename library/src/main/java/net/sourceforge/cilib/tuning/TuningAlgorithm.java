/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning;

import fj.data.List;
import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.problem.solution.*;
import net.sourceforge.cilib.tuning.parameterlist.ParameterListProvider;
import net.sourceforge.cilib.type.types.container.Vector;

public class TuningAlgorithm extends AbstractAlgorithm {

    private List<Vector> parameterList;
    private Vector currentParameters;
    private ParameterListProvider parameterProvider;
    private IterationStrategy<TuningAlgorithm> iterationStrategy;
    
    public TuningAlgorithm() {
        this.iterationStrategy = new FRaceIterationStrategy();
    }
    
    @Override
    public TuningAlgorithm getClone() {
        return this;
    }
    
    @Override
    public void algorithmInitialisation() {
        this.parameterList = parameterProvider._1();
    }

    @Override
    protected void algorithmIteration() {
        TuningProblem p = (TuningProblem) optimisationProblem;
        p.nextProblem();
        iterationStrategy.performIteration(this);
    }

    @Override
    public OptimisationSolution getBestSolution() {
        return getSolutions().iterator().next();
    }

    @Override
    public Iterable<OptimisationSolution> getSolutions() {
        java.util.List<OptimisationSolution> sols = new ArrayList();
        for (Vector v : parameterList) {
            sols.add(new OptimisationSolution(v, InferiorFitness.instance()));
        }
        
        if(sols.isEmpty()) {
            sols.add(new OptimisationSolution(Vector.of(), InferiorFitness.instance()));
        }
        
        return sols;
    }

    public void setParameterList(List<Vector> parameterList) {
        this.parameterList = parameterList;
    }

    public List<Vector> getParameterList() {
        return parameterList;
    }

    public void setCurrentParameters(Vector currentParameters) {
        this.currentParameters = currentParameters;
    }

    public Vector getCurrentParameters() {
        return currentParameters;
    }

    public void setParameterProvider(ParameterListProvider parameterProvider) {
        this.parameterProvider = parameterProvider;
    }

    public ParameterListProvider getParameterProvider() {
        return parameterProvider;
    }

    public void setIterationStrategy(IterationStrategy<TuningAlgorithm> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public IterationStrategy<TuningAlgorithm> getIterationStrategy() {
        return iterationStrategy;
    }

    public Fitness evaluate(Vector a) {
        this.currentParameters = a;
        return optimisationProblem.getFitness(a);
    }
}
