/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.nn.architecture.builder.CascadeArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.architecture.visitors.CascadeVisitor;
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

public class InitialiseNaNElementsReactionStrategyTest {

    @Test
    public void responseExecution() {
        NNDataTrainingProblem problem = new NNDataTrainingProblem();
        problem.getDataTableBuilder().setDataReader(new ARFFFileReader());
        problem.getDataTableBuilder().setSourceURL("library/src/test/resources/datasets/iris.arff");
        problem.setTrainingSetPercentage(0.7);
        problem.setGeneralisationSetPercentage(0.3);

        problem.getNeuralNetwork().getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        problem.getNeuralNetwork().setOperationVisitor(new CascadeVisitor());
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(4));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        problem.initialise();

        PSO pso = new PSO();
        pso.getInitialisationStrategy().setEntityType(new DynamicParticle());
        pso.addStoppingCondition(new MeasuredStoppingCondition());
        pso.setOptimisationProblem(problem);
        pso.performInitialisation();

        CascadeNetworkExpansionReactionStrategy firstReaction = new CascadeNetworkExpansionReactionStrategy();
        InitialiseNaNElementsReactionStrategy secondReaction = new InitialiseNaNElementsReactionStrategy();

        Assert.assertEquals(5, ((Vector) pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(5, problem.getNeuralNetwork().getWeights().size());

        for (int i = 0; i < Topologies.getBestEntity(pso.getTopology()).getDimension(); ++i) {
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).set(i, Real.valueOf(0.0));
        }
        firstReaction.performReaction(pso);
        Assert.assertEquals(11, ((Vector) pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(11, problem.getNeuralNetwork().getWeights().size());
        Assert.assertEquals(Vector.of(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      Double.NaN),
                            (Vector) Topologies.getBestEntity(pso.getTopology()).getPosition());
        Assert.assertEquals(Vector.of(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      Double.NaN),
                            (Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity());
        Assert.assertEquals(Vector.of(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      Double.NaN),
                            (Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition());

        secondReaction.performReaction(pso);
        Assert.assertEquals(11, ((Vector) pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(11, problem.getNeuralNetwork().getWeights().size());
        Vector position = (Vector) Topologies.getBestEntity(pso.getTopology()).getPosition();
        for (int curElement = 0; curElement < position.size(); ++curElement) {
            Assert.assertTrue(!Double.isNaN(position.doubleValueOf(curElement)));
        }
        Vector bestPosition = (Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition();
        for (int curElement = 0; curElement < bestPosition.size(); ++curElement) {
            Assert.assertTrue(!Double.isNaN(bestPosition.doubleValueOf(curElement)));
        }
        Vector velocity = (Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity();
        for (int curElement = 0; curElement < velocity.size(); ++curElement) {
            Assert.assertTrue(!Double.isNaN(velocity.doubleValueOf(curElement)));
        }
    }
}
