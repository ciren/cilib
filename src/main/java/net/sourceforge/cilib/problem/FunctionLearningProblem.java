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
package net.sourceforge.cilib.problem;

// TODO: Add domain validators to check that this is working on ContinuousFunctions

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.sourceforge.cilib.functions.AbstractFunction;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 *
 */
public class FunctionLearningProblem extends OptimisationProblemAdapter {
    private static final long serialVersionUID = -8903112361933892141L;

    private Function<Type, Double> function;
    private int sampleSetSize = 1000;
    private double trainingSetPercentage = 0.7;
    private double testingSetPercentage = 1.0 - trainingSetPercentage;
    private List<Double[]> testingSet = new ArrayList<Double[]>();
    private List<Double []> trainingSet = new ArrayList<Double[]>();
    private Random random = new MersenneTwister(System.currentTimeMillis());
    private NeuralNetworkProblem neuralNetwork = null;
    //private Compound domain = null;
    private double functionMaxValue = -Double.MAX_VALUE;

    public FunctionLearningProblem() {
    }

    public FunctionLearningProblem(
        AbstractFunction function,
        int sampleSetSize,
        double trainingSetPercentage,
        NeuralNetworkProblem neuralNetwork) {
        this.function = function;
        this.sampleSetSize = sampleSetSize;
        this.trainingSetPercentage = trainingSetPercentage;
        this.testingSetPercentage = 1.0 - trainingSetPercentage;
        this.neuralNetwork = neuralNetwork;
        initialise();
    }

    public FunctionLearningProblem(FunctionLearningProblem copy) {

    }

    public FunctionLearningProblem getClone() {
        return new FunctionLearningProblem(this);
    }

    public void initialise() {
        if (function == null) {
            throw new RuntimeException("the function is null");
        }
        if (sampleSetSize < 0) {
            throw new RuntimeException("sample set size is less than zero");
        }
        if (trainingSetPercentage < 0 || trainingSetPercentage > 1.0) {
            throw new RuntimeException("invalid training set percentage");
        }
        if (testingSetPercentage < 0 || testingSetPercentage > 1.0) {
            throw new RuntimeException("invalid test set percentage");
        }
        if (neuralNetwork == null) {
            throw new RuntimeException("neuralNetwork is null");
        }
/*        if (neuralNetwork.getSizeIL() - 1 != function.getDimension()) {
            throw new RuntimeException("input layer size does not match function dimension");
        }
        if (neuralNetwork.getSizeOL() != 1) {
            throw new RuntimeException("output layer size must be 1");
        }*/

        // add the required number of samples to the trainingSet.
        net.sourceforge.cilib.type.types.container.Vector vector = (net.sourceforge.cilib.type.types.container.Vector) this.getDomain().getBuiltRepresenation();
        while (trainingSet.size() < sampleSetSize * trainingSetPercentage) {
            // create a random point within the function domain.
            Double[] p = new Double[function.getDimension()];
            for (int i = 0; i < p.length; i++) {
                Real real = (Real) vector.get(0);
                double r = real.getBounds().getUpperBound() * random.nextDouble() -
                    2.0 * real.getBounds().getLowerBound() * random.nextDouble();
                    //((Quantitative) domain.getComponent(0)).getUpperBound().doubleValue() * random.nextDouble()
                        //- 2.0 * ((Quantitative) domain.getComponent(0)).getLowerBound().doubleValue() * random.nextDouble();
                p[i] = new Double(r);
            }

            // evaluate the input to determine the largest value for the function.
            Vector input = convertDoubleArray(p);
            double result = (function.evaluate(input)).doubleValue();
            if (result > functionMaxValue) {
                functionMaxValue = result;
            }

            // add the solution to the training set.
            trainingSet.add(p);
        }

        //Quantitative domain = (Quantitative) function.getDomainComponent().getComponent(0);
        //Domain domain = Domain.getInstance();

        // add the required number of samples to the testingSet.
        while (testingSet.size() < sampleSetSize * testingSetPercentage) {
            // create a random point within the function domain.
            Double[] p = new Double[function.getDimension()];
            for (int i = 0; i < p.length; i++) {
                double r =
                    //domain.getUpperBound().doubleValue() * random.nextDouble()
                    //    - 2.0 * domain.getLowerBound().doubleValue() * random.nextDouble();
                    //domain.getUpperBound() * random.nextDouble()
                    /*      - 2.0 * domain.getLowerBound() **/ random.nextDouble();
                p[i] = new Double(r);
            }

            // evaluate the input to determine the largest value for the function.
            Vector input = convertDoubleArray(p);
            double result = (function.evaluate(input)).doubleValue();
            if (result > functionMaxValue) {
                functionMaxValue = result;
            }

            // add the point to the training set.
            testingSet.add(p);
        }
    }

    /**
     * This bases the fitness of the solution on the trainingSet samples.
     * @param solution
     * @return The average of all fitness values from the trainingSample set
     */
    protected Fitness calculateFitness(Type solution) {
        //double[] tmp = (double[]) solution;
        // determine if the solution matches the number of weights required for
        // the NN to function.
/*        if (tmp.length != neuralNetwork.getNumberOfWeights()) {
            throw new RuntimeException("size of the solution does not match the number of weights required for the NN");
        }*/

        double totalFitness = 0.0;
        Iterator<Double[]> iterator = trainingSet.iterator();
        while (iterator.hasNext()) {
            // get the input value from the training set.
            Double[] p = iterator.next();

            // change the Double[] to a double[] for input to the NN.
            Vector input = convertDoubleArray(p);

            // calculate the expected output for the input.
            Fitness[] exp_output = new Fitness[1];
            exp_output[0] = new MinimisationFitness(function.evaluate(input));

            // we need to scale the expected output in the range [0.0, 1.0] for the
            // NN to be evaluated properly. Since the NN only outputs values in the
            // range [0.0, 0.1].
            exp_output[0] =new MinimisationFitness(new Double(((Double) exp_output[0].getValue()).doubleValue() / functionMaxValue));
            //      double slope = 0.05;
            //      double range = 1.0;
            //      exp_output[0] = sigmoid(exp_output[0], slope, range);

            // evaluate solution using the NN.
            double[] output = new double[1];
//            neuralNetwork.getOutput(input, tmp, output);

            // decrement the totalFitness.
            totalFitness -= Math.pow(((Double) exp_output[0].getValue()).doubleValue() - output[0], 2.0);
        }

        return new MinimisationFitness(new Double((totalFitness / trainingSet.size())));
    }

    public double getError(Object solution) {
        //double[] tmp = (double[]) solution;
        // determine if the solution matches the number of weights required for
        // the NN to function.
  /*      if (tmp.length != neuralNetwork.getNumberOfWeights()) {
            throw new RuntimeException("size of the solution does not match the number of weights required for the NN");
        }*/

        double totalFitness = 0.0;
        Iterator<Double[]> iterator = testingSet.iterator();
        while (iterator.hasNext()) {
            // get the input value from the training set.
            Double[] p = iterator.next();

            // change the Double[] to a double[] for input to the NN.
            Vector input = convertDoubleArray(p);

            // calculate the expected output for the input.
            double[] exp_output = new double[1];
            exp_output[0] = ((Double) function.evaluate(input)).doubleValue();

            // evaluate solution using the NN.
            double[] output = new double[1];
//            neuralNetwork.getOutput(input, tmp, output);

            // decrement the totalFitness.
            totalFitness -= Math.pow(exp_output[0] - output[0], 2.0);
        }

        return (totalFitness / testingSet.size());
    }

    private Vector convertDoubleArray(Double[] oldArray) {
        // create memory for the new array.
        Vector vector = new Vector(oldArray.length);
//        double[] newArray = new double[oldArray.length];

        // convert the Double objects into primitive doubles.
        for (int i = 0; i < oldArray.length; i++) {
            vector.add(new Real(oldArray[i]));
//            newArray[i] = oldArray[i].doubleValue();
        }

        return vector;
    }

    /*public DomainComponent getDomain() {
        return domain;
    }*/
    /*public Domain getDomain() {
        return Domain.getInstance();
    }*/

    public void setFunction(AbstractFunction function) {
        this.function = function;
    }

    public Function<Type, Double> getFunction() {
        return function;
    }

    public void setTestingSetPercentage(double testingSetPercentage) {
        this.testingSetPercentage = testingSetPercentage;
    }

    public double getTestingSetPercentage() {
        return testingSetPercentage;
    }

    public void setTrainingSetPercentage(double trainingSetPercentage) {
        this.trainingSetPercentage = trainingSetPercentage;
    }

    public double getTrainingSetPercentage() {
        return trainingSetPercentage;
    }

    public List<Double[]> getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(List<Double[]> trainingSet) {
        this.trainingSet = trainingSet;
    }

    public List<Double[]> getTestingSet() {
        return testingSet;
    }

    public void setTestingSet(List<Double[]> testingSet) {
        this.testingSet = testingSet;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void setSampleSetSize(int sampleSetSize) {
        this.sampleSetSize = sampleSetSize;
    }

    public int getSampleSetSize() {
        return sampleSetSize;
    }

    public DomainRegistry getDomain() {
        // TODO Auto-generated method stub
        return null;
    }

}
