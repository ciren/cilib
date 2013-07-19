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

public class PresetNeuronDomainTest {

    @Test
    public void testGenerateDomain() {
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-0.888:5.987)");
        PresetNeuronDomain provider = new PresetNeuronDomain();
        provider.setWeightDomainPrototype(domain);
        
        assertEquals("R(-0.888:5.987)^4", provider.generateDomain(4).getDomainString());
        assertEquals("R(-0.888:5.987)^10", provider.generateDomain(10).getDomainString());
    }
}
