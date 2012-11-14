/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm;

import java.util.ArrayList;
import net.sourceforge.cilib.io.DelimitedTextFileReader;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * Created for the purpose of getting output from a NN with pre-defined weights.
 * @todo Make is possible to train a NN with pre-trained weights: deep learning!
 * @todo Convert this into a decorator!
 * @author anna
 */
public class PretrainedWeightsSettingAlgorithm extends AbstractAlgorithm implements SingularAlgorithm {

    private DelimitedTextFileReader fileReader;
   
    @Override
    public void algorithmInitialisation() {
        NNTrainingProblem problem = (NNTrainingProblem) getOptimisationProblem();
        problem.initialise();

        Builder builder = Vector.newBuilder();
         try {
            fileReader.open();
            while(fileReader.hasNextRow()) {
                builder.add(Double.parseDouble(fileReader.nextRow().get(0).getString()));
            }
            fileReader.close();
        }
        catch (CIlibIOException ex) {
            ex.printStackTrace();
        }

        problem.getNeuralNetwork().setWeights(builder.build());
    }

    @Override
    protected void algorithmIteration() {
        // do nothing, just as promised!
    }

    @Override
    public OptimisationSolution getBestSolution() {
        NNTrainingProblem problem = (NNTrainingProblem) getOptimisationProblem();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        return new OptimisationSolution(neuralNetwork.getWeights(),  new MinimisationFitness(0.0));
    }

    @Override
    public Iterable<OptimisationSolution> getSolutions() {
        ArrayList<OptimisationSolution> list = new ArrayList<OptimisationSolution>();
        NNTrainingProblem problem = (NNTrainingProblem) getOptimisationProblem();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        list.add(new OptimisationSolution(neuralNetwork.getWeights(), new MinimisationFitness(0.0)));
        return list;
    }

    @Override
    public Object getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DelimitedTextFileReader getFileReader() {
        return fileReader;
    }

    public void setFileReader(DelimitedTextFileReader fileReader) {
        this.fileReader = fileReader;
    }

}