/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.NeuralNetworks;
import net.sourceforge.cilib.nn.NeuralNetworksTestHelper;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.StructuredType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LambdaGammaDomainInitialisationStrategyTest {

    private NeuralNetwork neuralNetwork;

    @Before
    public void setup() {
        neuralNetwork = NeuralNetworksTestHelper.createFFNN(3, 2, 1);
    }

    @Test
    public void shouldInitialiseDomain() {
        final DomainRegistry domainRegistry = new LambdaGammaDomainInitialisationStrategy().initialiseDomain(neuralNetwork);
        final StructuredType builtRepresenation = domainRegistry.getBuiltRepresentation();
        int idx = 0;
        final int weights = NeuralNetworks.countWeights(neuralNetwork);
        final int activationFuncCount = NeuralNetworks.countActivationFunctions(neuralNetwork);
        assertEquals(weights + activationFuncCount * 2, builtRepresenation.size());
        for (Object object : builtRepresenation) {
            Real real = (Real) object;
            if (idx < weights) {
                assertEquals(-3, real.getBounds().getLowerBound(), Maths.EPSILON);
                assertEquals(3, real.getBounds().getUpperBound(), Maths.EPSILON);
            } else {
                assertEquals(0.0, real.getBounds().getLowerBound(), Maths.EPSILON);
                assertEquals(1.0, real.getBounds().getUpperBound(), Maths.EPSILON);
            }
            idx++;
        }
    }
}
