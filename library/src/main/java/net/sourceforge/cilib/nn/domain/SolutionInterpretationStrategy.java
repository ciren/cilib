package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.visitors.ArchitectureVisitor;
import net.sourceforge.cilib.type.types.Type;

public interface SolutionInterpretationStrategy {

    SolutionInterpretationStrategy initialize(NeuralNetwork neuralNetwork);

    ArchitectureVisitor interpretSolution(Type solution);
}
