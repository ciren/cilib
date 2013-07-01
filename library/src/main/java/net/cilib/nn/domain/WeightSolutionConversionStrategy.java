/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.nn.domain;

import net.cilib.nn.NeuralNetwork;
import net.cilib.nn.architecture.visitors.ArchitectureVisitor;
import net.cilib.nn.architecture.visitors.WeightSettingVisitor;
import net.cilib.type.types.Type;
import net.cilib.type.types.container.Vector;

public class WeightSolutionConversionStrategy implements SolutionConversionStrategy {

    @Override
    public SolutionConversionStrategy initialise(NeuralNetwork neuralNetwork) {
        return this;
    }

    @Override
    public ArchitectureVisitor interpretSolution(Type solution) {
        return new WeightSettingVisitor((Vector) solution);
    }
}
