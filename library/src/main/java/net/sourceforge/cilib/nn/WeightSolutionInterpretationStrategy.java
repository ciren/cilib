package net.sourceforge.cilib.nn;

import net.sourceforge.cilib.nn.architecture.visitors.ArchitectureVisitor;
import net.sourceforge.cilib.nn.architecture.visitors.WeightSettingVisitor;
import net.sourceforge.cilib.nn.domain.SolutionInterpretationStrategy;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

public class WeightSolutionInterpretationStrategy implements SolutionInterpretationStrategy {

    @Override
    public ArchitectureVisitor interpretSolution(Type solution) {
        WeightSettingVisitor visitor = new WeightSettingVisitor();
        visitor.setWeights((Vector) solution);
        return visitor;
    }
}
