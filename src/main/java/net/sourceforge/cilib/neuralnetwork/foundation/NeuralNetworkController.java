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

import java.io.IOException;
import java.util.List;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.SingularAlgorithm;
import net.sourceforge.cilib.neuralnetwork.foundation.postSimulation.PostMeasurementSuite;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.Problem;

/**
 * @author stefanv
 * @deprecated This class is no longer viable - Please see {@linkplain EvaluationMediator}
 */
@Deprecated
public class NeuralNetworkController extends AbstractAlgorithm implements SingularAlgorithm {
    private static final long serialVersionUID = -904395696777716473L;
    protected NeuralNetworkProblem problem = null;
    protected NNError[] errorDt = null;
    protected PostMeasurementSuite measures;

    public NeuralNetworkController() {
        super();
        this.problem = null;
        this.errorDt = null;
        this.measures = null;
    }

    public NeuralNetworkController(NeuralNetworkController rhs) {
//        super(rhs);
        throw new UnsupportedOperationException("public NeuralNetworkController(NeuralNetworkController rhs)");
    }

    public NeuralNetworkController getClone() {
        return new NeuralNetworkController(this);
    }

    public void performInitialisation() {

        if (this.problem == null){
            throw new IllegalArgumentException("NeuralNetworkController: Required NNProblem object was null during initialization");
        }

        this.problem.initialize();
    }

    public void performUninitialisation() {

        if (this.measures != null){
            try {
                measures.performMeasurement();
            }
            catch (IOException e) {
                throw new IllegalStateException("Problem writing Simulation measures to file");
            }
        }

    }

    public void algorithmIteration() {

        errorDt = problem.learningEpoch();
        System.out.println("------------   Epoch " + this.getIterations() + " completed, error list :   ------------");
        for (int i = 0; i < errorDt.length; i++) {
            System.out.println("\t" + errorDt[i].getName() + " \t\t\t" + ((Double) errorDt[i].getValue()).doubleValue());
        }
    }

    public NNError[] getError() {
        return errorDt;
    }

    public void setProblem(Problem problem) {
        this.problem = (NeuralNetworkProblem) problem;
    }

    public void setOptimisationProblem(OptimisationProblem problem) {
        this.problem = (NeuralNetworkProblem) problem;
    }

    public OptimisationProblem getOptimisationProblem() {
        return this.problem;
    }

    public void setMeasures(PostMeasurementSuite measures) {
        this.measures = measures;
    }

    public OptimisationSolution getBestSolution() {
        return null;
    }

    public List<OptimisationSolution> getSolutions() {
        return null;
    }

}
