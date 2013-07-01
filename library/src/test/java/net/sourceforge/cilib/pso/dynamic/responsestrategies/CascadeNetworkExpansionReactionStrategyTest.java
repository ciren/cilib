/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.pso.dynamic.responsestrategies;

import net.cilib.entity.Topologies;
import net.cilib.io.ARFFFileReader;
import net.cilib.io.transform.PatternConversionOperator;
import net.cilib.nn.architecture.builder.CascadeArchitectureBuilder;
import net.cilib.nn.architecture.builder.LayerConfiguration;
import net.cilib.nn.architecture.visitors.CascadeVisitor;
import net.cilib.problem.nn.NNDataTrainingProblem;
import net.cilib.pso.dynamic.DynamicParticle;
import net.cilib.pso.PSO;
import net.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.cilib.type.types.container.Vector;
import net.cilib.type.types.Real;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class CascadeNetworkExpansionReactionStrategyTest {

    /**
     *
     */
    @Test
    public void algorithmExecution() {
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

        CascadeNetworkExpansionReactionStrategy reaction = new CascadeNetworkExpansionReactionStrategy();

        Assert.assertEquals(5, ((Vector)pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(5, problem.getNeuralNetwork().getWeights().size());

        for (int i = 0; i < Topologies.getBestEntity(pso.getTopology()).getDimension(); ++i) {
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).set(i, Real.valueOf(0.0));
        }
        reaction.performReaction(pso);
        Assert.assertEquals(11, ((Vector)pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(11, problem.getNeuralNetwork().getWeights().size());
        Assert.assertEquals(Vector.of(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getPosition());
        Assert.assertEquals(Vector.of(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getVelocity());
        Assert.assertEquals(Vector.of(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getBestPosition());

        for (int i = 0; i < Topologies.getBestEntity(pso.getTopology()).getDimension(); ++i) {
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).set(i, Real.valueOf(0.0));
        }
        reaction.performReaction(pso);
        Assert.assertEquals(18, ((Vector)pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(18, problem.getNeuralNetwork().getWeights().size());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN,
                                      Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getPosition());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN,
                                      Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getVelocity());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN,
                                      Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getBestPosition());

        for (int i = 0; i < Topologies.getBestEntity(pso.getTopology()).getDimension(); ++i) {
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).set(i, Real.valueOf(0.0));
        }
        reaction.performReaction(pso);
        Assert.assertEquals(26, ((Vector)pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(26, problem.getNeuralNetwork().getWeights().size());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      0.0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0,
                                      0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getPosition());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      0.0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0,
                                      0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getVelocity());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      0.0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0,
                                      0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getBestPosition());
    }

    /**
     *
     */
    @Test
    public void algorithmExecution2() {
        NNDataTrainingProblem problem = new NNDataTrainingProblem();
        PatternConversionOperator patternConversion = new PatternConversionOperator();
        patternConversion.setClassLength(2);
        problem.setPatternConversionOperator(patternConversion);
        problem.getDataTableBuilder().setDataReader(new ARFFFileReader());
        problem.getDataTableBuilder().setSourceURL("library/src/test/resources/datasets/iris.arff");
        problem.setTrainingSetPercentage(0.7);
        problem.setGeneralisationSetPercentage(0.3);

        problem.getNeuralNetwork().getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        problem.getNeuralNetwork().setOperationVisitor(new CascadeVisitor());
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        problem.initialise();

        PSO pso = new PSO();
        pso.getInitialisationStrategy().setEntityType(new DynamicParticle());
        pso.addStoppingCondition(new MeasuredStoppingCondition());
        pso.setOptimisationProblem(problem);
        pso.performInitialisation();

        CascadeNetworkExpansionReactionStrategy reaction = new CascadeNetworkExpansionReactionStrategy();

        Assert.assertEquals(8, ((Vector)pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(8, problem.getNeuralNetwork().getWeights().size());

        for (int i = 0; i < Topologies.getBestEntity(pso.getTopology()).getDimension(); ++i) {
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).set(i, Real.valueOf(0.0));
        }
        reaction.performReaction(pso);
        Assert.assertEquals(14, ((Vector)pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(14, problem.getNeuralNetwork().getWeights().size());
        Assert.assertEquals(Vector.of(Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, Double.NaN, 0.0,
                                      0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getPosition());
        Assert.assertEquals(Vector.of(Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, Double.NaN, 0.0,
                                      0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getVelocity());
        Assert.assertEquals(Vector.of(Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, Double.NaN, 0.0,
                                      0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getBestPosition());

        for (int i = 0; i < Topologies.getBestEntity(pso.getTopology()).getDimension(); ++i) {
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).set(i, Real.valueOf(0.0));
        }
        reaction.performReaction(pso);
        Assert.assertEquals(21, ((Vector)pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(21, problem.getNeuralNetwork().getWeights().size());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0,
                                      0.0, 0.0, 0.0, 0.0, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getPosition());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0,
                                      0.0, 0.0, 0.0, 0.0, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getVelocity());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0,
                                      0.0, 0.0, 0.0, 0.0, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getBestPosition());

        for (int i = 0; i < Topologies.getBestEntity(pso.getTopology()).getDimension(); ++i) {
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getPosition()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getVelocity()).set(i, Real.valueOf(0.0));
            ((Vector) Topologies.getBestEntity(pso.getTopology()).getBestPosition()).set(i, Real.valueOf(0.0));
        }
        reaction.performReaction(pso);
        Assert.assertEquals(29, ((Vector)pso.getBestSolution().getPosition()).size());
        Assert.assertEquals(29, problem.getNeuralNetwork().getWeights().size());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN,
                                      Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      0.0, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getPosition());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN,
                                      Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      0.0, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getVelocity());
        Assert.assertEquals(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN,
                                      Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0,
                                      0.0, Double.NaN, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.NaN),
                            (Vector)Topologies.getBestEntity(pso.getTopology()).getBestPosition());
    }
}
