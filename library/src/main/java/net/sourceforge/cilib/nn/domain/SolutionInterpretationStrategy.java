package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.nn.architecture.visitors.ArchitectureVisitor;
import net.sourceforge.cilib.type.types.Type;

public interface SolutionInterpretationStrategy {

    public ArchitectureVisitor interpretSolution(Type solution);

}
