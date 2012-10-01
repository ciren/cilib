package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.type.DomainRegistry;

public interface DomainInitializationStrategy {

    DomainRegistry initializeDomain(NeuralNetwork neuralNetwork);

}
