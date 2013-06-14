/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LambdaGammaNeuronDomainTest {

    @Test
    public void testGenerateDomain() {
        FaninNeuronDomain subProvider = new FaninNeuronDomain();
        
        StringBasedDomainRegistry lambdaDomain = new StringBasedDomainRegistry();
        lambdaDomain.setDomainString("R(-0.888:5.987)");
        StringBasedDomainRegistry gammaDomain = new StringBasedDomainRegistry();
        gammaDomain.setDomainString("R(-0.8:0.9)");
        
        LambdaGammaNeuronDomain provider = new LambdaGammaNeuronDomain();
        provider.setProvider(subProvider);
        provider.setLambdaDomainPrototype(lambdaDomain);
        provider.setGammaDomainPrototype(gammaDomain);
        
        assertEquals("R(-0.5:0.5)^4,R(-0.888:5.987),R(-0.8:0.9)", provider.generateDomain(4).getDomainString());
        assertEquals("R(-0.25:0.25)^16,R(-0.888:5.987),R(-0.8:0.9)", provider.generateDomain(16).getDomainString());
    }
}
