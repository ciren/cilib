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

public class ReinitialiseCascadeNetworkOutputWeightsReactionStrategyTest {

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
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        problem.initialise();

        PSO pso = new PSO();
        pso.getInitialisationStrategy().setEntityType(new DynamicParticle());
        pso.addStoppingCondition(new MeasuredStoppingCondition());
        pso.setOptimisationProblem(problem);
        pso.performInitialisation();

        ReinitialiseCascadeNetworkOutputWeightsReactionStrategy reaction = new ReinitialiseCascadeNetworkOutputWeightsReactionStrategy();

        Assert.assertEquals(26, ((Vector) pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(26, problem.getNeuralNetwork().getWeights().size());

        for (int i = 0; i < Topologies.getBestEntity(pso.getTopology()).getDimension(); ++i) {
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).set(i, Real.valueOf(Double.NaN));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).set(i, Real.valueOf(Double.NaN));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).set(i, Real.valueOf(Double.NaN));
        }

        reaction.performReaction(pso);
        Assert.assertEquals(26, ((Vector) pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(26, problem.getNeuralNetwork().getWeights().size());

        for (int i = 0; i < 18; ++i) {
            Assert.assertTrue(Double.isNaN(((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).doubleValueOf(i)));
            Assert.assertTrue(Double.isNaN(((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).doubleValueOf(i)));
            Assert.assertTrue(Double.isNaN(((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).doubleValueOf(i)));
        }

        for (int i = 18; i < 26; ++i) {
            Assert.assertTrue(!Double.isNaN(((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).doubleValueOf(i)));
            Assert.assertTrue(((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).doubleValueOf(i) == 0.0);
            Assert.assertTrue(Double.isNaN(((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).doubleValueOf(i)));
        }
    }

    @Test
    public void responseExecutionComplexCascadeArchitecture() {
        NNDataTrainingProblem problem = new NNDataTrainingProblem();
        problem.getDataTableBuilder().setDataReader(new ARFFFileReader());
        problem.getDataTableBuilder().setSourceURL("library/src/test/resources/datasets/iris.arff");
        problem.setTrainingSetPercentage(0.7);
        problem.setGeneralisationSetPercentage(0.3);

        problem.getNeuralNetwork().getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        problem.getNeuralNetwork().setOperationVisitor(new CascadeVisitor());
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(4));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        problem.initialise();

        PSO pso = new PSO();
        pso.getInitialisationStrategy().setEntityType(new DynamicParticle());
        pso.addStoppingCondition(new MeasuredStoppingCondition());
        pso.setOptimisationProblem(problem);
        pso.performInitialisation();

        ReinitialiseCascadeNetworkOutputWeightsReactionStrategy reaction = new ReinitialiseCascadeNetworkOutputWeightsReactionStrategy();

        Assert.assertEquals(25, ((Vector)pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(25, problem.getNeuralNetwork().getWeights().size());

        for (int i = 0; i < Topologies.getBestEntity(pso.getTopology()).getDimension(); ++i) {
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).set(i, Real.valueOf(Double.NaN));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).set(i, Real.valueOf(Double.NaN));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).set(i, Real.valueOf(Double.NaN));
        }

        reaction.performReaction(pso);
        Assert.assertEquals(25, ((Vector)pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(25, problem.getNeuralNetwork().getWeights().size());

        for (int i = 0; i < 17; ++i) {
            Assert.assertTrue(Double.isNaN(((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).doubleValueOf(i)));
            Assert.assertTrue(Double.isNaN(((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).doubleValueOf(i)));
            Assert.assertTrue(Double.isNaN(((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).doubleValueOf(i)));
        }

        for (int i = 17; i < 25; ++i) {
            Assert.assertTrue(!Double.isNaN(((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).doubleValueOf(i)));
            Assert.assertTrue(((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).doubleValueOf(i) == 0.0);
            Assert.assertTrue(Double.isNaN(((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).doubleValueOf(i)));
        }
    }
}
