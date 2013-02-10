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
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This reaction strategy reinitialises the particles' dimensions corresponding
 * to the output weights of a cascade network. The elements in the position
 * vector are reinitialised, the elements in the velocity vector are set to zero
 * and the elements in the personal best vector are left as is.
 *
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class ReinitialiseCascadeNetworkOutputWeightsReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {

    public ReinitialiseCascadeNetworkOutputWeightsReactionStrategy() {
    }

    public ReinitialiseCascadeNetworkOutputWeightsReactionStrategy(ReinitialiseCascadeNetworkOutputWeightsReactionStrategy<E> rhs) {
        super(rhs);
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
        NNDataTrainingProblem problem = (NNDataTrainingProblem) algorithm.getOptimisationProblem();
        NeuralNetwork network = problem.getNeuralNetwork();

        int precedingLayersSize = 0;
        for (int curLayer = 0; curLayer < network.getArchitecture().getArchitectureBuilder().getLayerConfigurations().size()-1; ++curLayer) {
            precedingLayersSize += network.getArchitecture().getArchitectureBuilder().getLayerConfigurations().get(curLayer).getSize();
        }
        if (network.getArchitecture().getArchitectureBuilder().getLayerConfigurations().get(0).isBias())
            precedingLayersSize++;

        int outputLayerSize = network.getArchitecture().getArchitectureBuilder().getLayerConfigurations()
                              .get(network.getArchitecture().getArchitectureBuilder().getLayerConfigurations().size()-1).getSize();
        int nrOfweightsToDo = precedingLayersSize * outputLayerSize;

        Topology<? extends Entity> entities = algorithm.getTopology();

        for (Entity entity : entities) {
            DynamicParticle particle = (DynamicParticle) entity;

            Vector position = (Vector) particle.getPosition();
            Vector velocity = (Vector) particle.getVelocity();
            for (int curElement = position.size() - nrOfweightsToDo; curElement < position.size(); ++curElement) {
                ((Real) position.get(curElement)).randomise();
                velocity.setReal(curElement, 0.0);
            }
        }
    }
}
