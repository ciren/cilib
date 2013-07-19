/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import org.junit.Test;
import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AreaUnderROCCurveTest {

    @Test
    public void testSingleOutput() {

		StandardPatternDataTable targets = new StandardPatternDataTable();
		targets.addRow(new StandardPattern(Vector.of(0.0), Real.valueOf(1.0)));
		targets.addRow(new StandardPattern(Vector.of(0.0), Real.valueOf(0.0)));
		targets.addRow(new StandardPattern(Vector.of(0.0), Real.valueOf(0.0)));
		targets.addRow(new StandardPattern(Vector.of(0.0), Real.valueOf(1.0)));
		targets.addRow(new StandardPattern(Vector.of(0.0), Real.valueOf(1.0)));
		targets.addRow(new StandardPattern(Vector.of(0.0), Real.valueOf(0.0)));

        NeuralNetwork network = mock(NeuralNetwork.class);
        when(network.evaluatePattern(any(StandardPattern.class))).thenReturn(Vector.of(0.9),
		                                                                    Vector.of(0.3),
		                                                                    Vector.of(0.6),
		                                                                    Vector.of(0.6),
		                                                                    Vector.of(0.7),
		                                                                    Vector.of(0.1));

        NNTrainingProblem problem = mock(NNTrainingProblem.class);
        when(problem.getGeneralisationSet()).thenReturn(targets);
        when(problem.getNeuralNetwork()).thenReturn(network);

        OptimisationSolution solution = new OptimisationSolution(Vector.of(), InferiorFitness.instance());

        Algorithm algorithm = mock(Algorithm.class);
        when(algorithm.getOptimisationProblem()).thenReturn(problem);
        when(algorithm.getBestSolution()).thenReturn(solution);

        AreaUnderROCCurve measurement = new AreaUnderROCCurve();

		assertEquals(0.9444444444444444,((Vector) measurement.getValue(algorithm)).doubleValueOf(0), Maths.EPSILON);
    }

    @Test
    public void testMultiOutput() {

		StandardPatternDataTable targets = new StandardPatternDataTable();
		targets.addRow(new StandardPattern(Vector.of(0.0), Vector.of(1.0, 1.0, 1.0)));
		targets.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0, 0.0, 1.0)));
		targets.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0, 1.0, 1.0)));
		targets.addRow(new StandardPattern(Vector.of(0.0), Vector.of(1.0, 0.0, 0.0)));
		targets.addRow(new StandardPattern(Vector.of(0.0), Vector.of(1.0, 0.0, 0.0)));
		targets.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0, 1.0, 0.0)));

        NeuralNetwork network = mock(NeuralNetwork.class);
        when(network.evaluatePattern(any(StandardPattern.class))).thenReturn(Vector.of(0.9, 0.1, 0.9),
		                                                                    Vector.of(0.3, 0.7, 0.1),
		                                                                    Vector.of(0.6, 0.4, 0.5),
		                                                                    Vector.of(0.6, 0.4, 0.5),
		                                                                    Vector.of(0.7, 0.9, 0.9),
		                                                                    Vector.of(0.1, 0.3, 0.1));

        NNTrainingProblem problem = mock(NNTrainingProblem.class);
        when(problem.getGeneralisationSet()).thenReturn(targets);
        when(problem.getNeuralNetwork()).thenReturn(network);

        OptimisationSolution solution = new OptimisationSolution(Vector.of(), InferiorFitness.instance());

        Algorithm algorithm = mock(Algorithm.class);
        when(algorithm.getOptimisationProblem()).thenReturn(problem);
        when(algorithm.getBestSolution()).thenReturn(solution);

        AreaUnderROCCurve measurement = new AreaUnderROCCurve();

        Vector result = ((Vector) measurement.getValue(algorithm));
		assertEquals(0.9444444444444444,result.doubleValueOf(0), Maths.EPSILON);
		assertEquals(0.0555555555555555,result.doubleValueOf(1), Maths.EPSILON);
		assertEquals(0.5,result.doubleValueOf(2), Maths.EPSILON);
    }

    @Test
    public void testAreaUnderCurve() {
        AreaUnderROCCurve measurement = new AreaUnderROCCurve();
        AreaUnderROCCurve measurement2 = new AreaUnderROCCurve();
        measurement2.setLowerBound(-2.0);
        measurement2.setUpperBound(2.0);

		ArrayList<Real> targets = new ArrayList<Real>();
		targets.add(Real.valueOf(0.9));
		targets.add(Real.valueOf(0.1));
		targets.add(Real.valueOf(0.1));
		targets.add(Real.valueOf(0.9));
		targets.add(Real.valueOf(0.9));
		targets.add(Real.valueOf(0.1));

		ArrayList<Real> outputs = new ArrayList<Real>();
		outputs.add(Real.valueOf(0.9));
		outputs.add(Real.valueOf(0.3));
		outputs.add(Real.valueOf(0.6));
		outputs.add(Real.valueOf(0.6));
		outputs.add(Real.valueOf(0.7));
		outputs.add(Real.valueOf(0.1));

		assertEquals(0.9444444444444444, measurement.areaUnderCurve(targets, outputs), Maths.EPSILON);
        
		ArrayList<Real> targets2 = new ArrayList<Real>();
		targets2.add(Real.valueOf(0.9));
		targets2.add(Real.valueOf(0.1));
		targets2.add(Real.valueOf(0.9));
		targets2.add(Real.valueOf(0.1));
		targets2.add(Real.valueOf(0.1));
		targets2.add(Real.valueOf(0.9));

		ArrayList<Real> outputs2 = new ArrayList<Real>();
		outputs2.add(Real.valueOf(0.1));
		outputs2.add(Real.valueOf(0.7));
		outputs2.add(Real.valueOf(0.4));
		outputs2.add(Real.valueOf(0.4));
		outputs2.add(Real.valueOf(0.9));
		outputs2.add(Real.valueOf(0.3));

		assertEquals(0.0555555555555555, measurement.areaUnderCurve(targets2, outputs2), Maths.EPSILON);
        
		ArrayList<Real> targets3 = new ArrayList<Real>();
		targets3.add(Real.valueOf(0.9));
		targets3.add(Real.valueOf(0.9));
		targets3.add(Real.valueOf(0.9));
		targets3.add(Real.valueOf(0.1));
		targets3.add(Real.valueOf(0.1));
		targets3.add(Real.valueOf(0.1));

		ArrayList<Real> outputs3 = new ArrayList<Real>();
		outputs3.add(Real.valueOf(0.0));
		outputs3.add(Real.valueOf(1.0));
		outputs3.add(Real.valueOf(0.5));
		outputs3.add(Real.valueOf(0.5));
		outputs3.add(Real.valueOf(0.0));
		outputs3.add(Real.valueOf(1.0));

		assertEquals(0.5, measurement.areaUnderCurve(targets3, outputs3), Maths.EPSILON);
        
		ArrayList<Real> targets4 = new ArrayList<Real>();
		targets4.add(Real.valueOf(0.9));
		targets4.add(Real.valueOf(0.9));
		targets4.add(Real.valueOf(0.9));
		targets4.add(Real.valueOf(-0.9));
		targets4.add(Real.valueOf(-0.9));
		targets4.add(Real.valueOf(-0.9));

		ArrayList<Real> outputs4 = new ArrayList<Real>();
		outputs4.add(Real.valueOf(2.0));
		outputs4.add(Real.valueOf(2.0));
		outputs4.add(Real.valueOf(2.0));
		outputs4.add(Real.valueOf(-2.0));
		outputs4.add(Real.valueOf(-2.0));
		outputs4.add(Real.valueOf(-2.0));

		assertEquals(1.0, measurement2.areaUnderCurve(targets4, outputs4), Maths.EPSILON);

		ArrayList<Real> outputs5 = new ArrayList<Real>();
		outputs5.add(Real.valueOf(-2.0));
		outputs5.add(Real.valueOf(-2.0));
		outputs5.add(Real.valueOf(-2.0));
		outputs5.add(Real.valueOf(2.0));
		outputs5.add(Real.valueOf(2.0));
		outputs5.add(Real.valueOf(2.0));

		assertEquals(0.0, measurement2.areaUnderCurve(targets4, outputs5), Maths.EPSILON);
    }
}
