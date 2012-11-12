/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.visitors;

import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.NeuralNetworksTestHelper;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.BiasNeuron;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LambdaGammaVisitorTest {

    private NeuralNetwork neuralNetwork;
    private Vector solution;
    private int weightCount;
    private int activationFunctionCount;

    @Before
    public void setup() {
        neuralNetwork = NeuralNetworksTestHelper.createFFNN(3, 2, 1);

        final Vector.Builder builder = Vector.newBuilder();
        final Vector weights = neuralNetwork.getWeights();
        weightCount = weights.size();
        activationFunctionCount = 3; // hidden + output
        Bounds bounds = new Bounds(0.0, 1.0);
        builder.copyOf(weights);
        builder.add(Real.valueOf(1.0, bounds)).add(Real.valueOf(1.1, bounds)).add(Real.valueOf(1.2, bounds));
        builder.add(Real.valueOf(0.7, bounds)).add(Real.valueOf(0.8, bounds)).add(Real.valueOf(0.9, bounds));
        solution = builder.build();
    }

    @Test
    public void shouldExtractWeights() {
        final Vector vector = new LambdaGammaVisitor(solution, weightCount, activationFunctionCount).extractWeights(solution);
        assertEquals(neuralNetwork.getWeights(), vector);
    }

    @Test
    public void shouldExtractLambdas() {
        final Vector vector = new LambdaGammaVisitor(solution, weightCount, activationFunctionCount).extractLambdas(solution);
        final Vector expected = Vector.of(1.0, 1.1, 1.2);
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).doubleValue(), vector.get(i).doubleValue(), Maths.EPSILON);
        }
    }

    @Test
    public void shouldExtractGammas() {
        final Vector vector = new LambdaGammaVisitor(solution, weightCount, activationFunctionCount).extractGammas(solution);
        final Vector expected = Vector.of(0.7, 0.8, 0.9);
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).doubleValue(), vector.get(i).doubleValue(), Maths.EPSILON);
        }
    }

    @Test
    public void shouldVisit() {
        final Vector.Builder builder = Vector.newBuilder();
        solution = builder.copyOf(solution).buildRandom();

        final LambdaGammaVisitor lambdaGammaVisitor = new LambdaGammaVisitor(solution, weightCount, activationFunctionCount);
        final Vector weights = lambdaGammaVisitor.extractWeights(solution);
        final Vector lambdas = lambdaGammaVisitor.extractLambdas(solution);
        final Vector gammas = lambdaGammaVisitor.extractGammas(solution);

        lambdaGammaVisitor.visit(neuralNetwork.getArchitecture());

        int lambdaIdx = 0;
        int gammaIdx = 0;
        int weightIdx = 0;

        for (Layer layer : neuralNetwork.getArchitecture().getActivationLayers()) {
            for (Neuron neuron : layer) {
                if (!(neuron instanceof BiasNeuron)) {
                    assertEquals(lambdas.get(lambdaIdx++).doubleValue(), ((Sigmoid) neuron.getActivationFunction()).getLambda().getParameter(), Maths.EPSILON);
                    assertEquals(gammas.get(gammaIdx++).doubleValue(), ((Sigmoid) neuron.getActivationFunction()).getGamma().getParameter(), Maths.EPSILON);
                }
                Vector neuronWeights = neuron.getWeights();
                int size = neuronWeights.size();
                for (int j = 0; j < size; j++) {
                    assertEquals(weights.get(weightIdx++), neuronWeights.get(j));
                }
            }
        }
    }

}
