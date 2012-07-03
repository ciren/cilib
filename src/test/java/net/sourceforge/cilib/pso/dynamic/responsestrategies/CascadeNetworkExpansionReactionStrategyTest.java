/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import static org.hamcrest.CoreMatchers.is;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.math.random.generator.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.math.random.generator.ZeroSeederStrategy;
import net.sourceforge.cilib.nn.architecture.builder.CascadeArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.visitors.CascadeVisitor;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.problem.NNDataTrainingProblem;
import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.entity.Topologies;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Bounds;

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
        problem.getDataTableBuilder().setSourceURL("src/test/resources/datasets/iris.arff");
        problem.setTrainingSetPercentage(0.7);
        problem.setGeneralizationSetPercentage(0.3);

		problem.getNeuralNetwork().getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
		problem.getNeuralNetwork().setOperationVisitor(new CascadeVisitor());
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(4));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(0));
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
}
