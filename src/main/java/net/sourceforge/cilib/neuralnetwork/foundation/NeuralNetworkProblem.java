/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.neuralnetwork.foundation;

import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.ProblemVisitor;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author stefanv
 * @deprecated This class is no longer really valid.
 */
@Deprecated
public class NeuralNetworkProblem implements OptimisationProblem, Initializable {
    private static final long serialVersionUID = -5790791148649131742L;
    protected int fitnessEvaluations;
    protected EvaluationMediator evaluationStrategy = null;

    public NeuralNetworkProblem(){
        this.evaluationStrategy = null;
        this.fitnessEvaluations = 0;
    }

    public NeuralNetworkProblem(NeuralNetworkProblem rhs) {
//        super(rhs);
        throw new UnsupportedOperationException("public NeuralNetworkProblem(NeuralNetworkProblem rhs)");
    }

    public NeuralNetworkProblem getClone() {
        return new NeuralNetworkProblem(this);
    }

    public void initialize(){

        if (this.evaluationStrategy == null) {
            throw new IllegalArgumentException("NeuralNetworkProblem: A required evaluationStrategy object was null during initialization");
        }

//        this.evaluationStrategy.initialize();
        this.evaluationStrategy.performInitialisation();

    }

    public NNError[] learningEpoch(){
        evaluationStrategy.performLearning();
        return evaluationStrategy.getErrorDt();
    }

    public TypeList evaluate(Vector in){
        StandardPattern p = new StandardPattern(in, null);
        return evaluationStrategy.evaluate(p);
    }

    @Override
    public Fitness getFitness(Type solution) {
        ++fitnessEvaluations;

        this.getTopology().setWeights((Vector) solution);

            //Defaults to first error element as the main fitness...
            return evaluationStrategy.getErrorDt()[0];
    }

    public int getFitnessEvaluations() {
        return this.fitnessEvaluations;
    }

    public NeuralNetworkTopology getTopology() {
        return evaluationStrategy.getTopology();
    }

    public void setTopology(NeuralNetworkTopology topology) {
        evaluationStrategy.setTopology(topology);
    }

    public EvaluationMediator getEvaluationStrategy() {
        return evaluationStrategy;
    }

    public DomainRegistry getDomain() {
        return null;
    }

    public void setEvaluationStrategy(EvaluationMediator evaluationStrategy) {
        this.evaluationStrategy = evaluationStrategy;
    }

    public DataSetBuilder getDataSetBuilder() {
        return null;
    }

    public void setDataSetBuilder(DataSetBuilder dataSetBuilder) {

    }

    public void accept(ProblemVisitor visitor) {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public void changeEnvironment() {
        throw new UnsupportedOperationException("This method is not implemented");
    }
}
