package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.visitors.ArchitectureVisitor;
import net.sourceforge.cilib.nn.architecture.visitors.WeightSettingVisitor;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

public class WeightSolutionConversionStrategy implements SolutionConversionStrategy {

    @Override
    public SolutionConversionStrategy initialize(NeuralNetwork neuralNetwork) {
        return this;
    }

    @Override
    public ArchitectureVisitor interpretSolution(Type solution) {
        return new WeightSettingVisitor((Vector) solution);
    }
}
