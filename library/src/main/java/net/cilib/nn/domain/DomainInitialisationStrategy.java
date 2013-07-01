/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.nn.domain;

import net.cilib.nn.NeuralNetwork;
import net.cilib.type.DomainRegistry;

public interface DomainInitialisationStrategy {

    DomainRegistry initialiseDomain(NeuralNetwork neuralNetwork);

}
