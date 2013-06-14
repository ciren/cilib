/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FaninNeuronDomainTest {

    @Test
    public void testGenerateDomain() {
        FaninNeuronDomain provider = new FaninNeuronDomain();
        
        assertEquals("R(-0.5:0.5)^4", provider.generateDomain(4).getDomainString());
        assertEquals("R(-0.25:0.25)^16", provider.generateDomain(16).getDomainString());
    }
}
