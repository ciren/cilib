/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.Real;

/**
 * This reaction strategy reinitialises the particles' dimensions corresponding
 * to the output weights of a cascade network. The elements in the position
 * vector are reinitilised, the elements in the velocity vector are set to zero
 * and the elements in the personal best vector are left as is.
 *
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class ReinitialiseCascadeNetworkOutputWeightsReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {
	protected RandomProvider randomGenerator = null;

	public ReinitialiseCascadeNetworkOutputWeightsReactionStrategy() {
		randomGenerator = new MersenneTwister();
    }

    public ReinitialiseCascadeNetworkOutputWeightsReactionStrategy(ReinitialiseCascadeNetworkOutputWeightsReactionStrategy<E> rhs) {
        super(rhs);
		randomGenerator = rhs.randomGenerator;
    }

    @Override
    public ReinitialiseCascadeNetworkOutputWeightsReactionStrategy<E> getClone() {
        return new ReinitialiseCascadeNetworkOutputWeightsReactionStrategy<E>(this);
    }

    /**
     * Reinitialises all the output weights of a cascade network within a PSO.
     *
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
    	NNDataTrainingProblem problem = (NNDataTrainingProblem)algorithm.getOptimisationProblem();
    	NeuralNetwork network = problem.getNeuralNetwork();

    	int precedingLayersSize = network.getArchitecture().getArchitectureBuilder()
    							.getLayerConfigurations().get(0).getSize()
								+ network.getArchitecture().getArchitectureBuilder()
    							.getLayerConfigurations().get(1).getSize();
		if (network.getArchitecture().getArchitectureBuilder().getLayerConfigurations().get(0).isBias())
			precedingLayersSize++;
		
    	int outputLayerSize = network.getArchitecture().getArchitectureBuilder()
    							.getLayerConfigurations().get(2).getSize();
		int nrOfweightsToDo = precedingLayersSize * outputLayerSize;

		Topology<? extends Entity> entities = algorithm.getTopology();
		
		for (Entity entity : entities) {
			DynamicParticle particle = (DynamicParticle)entity;
			
            Vector position = (Vector)particle.getPosition();
			Vector velocity = (Vector)particle.getVelocity();
			for (int curElement = position.size()-nrOfweightsToDo; curElement < position.size(); ++curElement) {
            	((Real) position.get(curElement)).randomize(randomGenerator);
				velocity.setReal(curElement, 0.0);
			}
        }
    }

    /**
     * Set the random number generator to use.
     *
     * @param r a {@link Random} object
     */
    protected void setRandomGenerator(RandomProvider r) {
        randomGenerator = r;
    }

    /**
     * Retrieve the random number generator being used.
     *
     * @return the {@link Random} object being used to generate a random sequence of numbers
     */
    protected RandomProvider getRandomGenerator() {
        return randomGenerator;
    }
}
