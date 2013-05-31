/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.simulator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Integration test to ensure that the construction of all provided
 * XML files succeed.
 */
public class SimulatorTest {

    /**
     * <p>
     * Iterate through all the available XML simulation specifications and
     * instantiate the needed objects, as well as making the simulations
     * ready for execution. After this process, discard the constructed
     * instances.
     * </p>
     * <p>
     * Tests will pass if all instance creation for the defined simulations
     * succeed.
     * </p>
     */
    @Test
    public void simulationConstruction() {
        for (String filename : getXMLFiles()) {
            System.out.println("Constructing specification: " + filename);
            List<Simulator> sims = SimulatorShell.prepare(new File("simulator/xml", filename));
            for (Simulator s : sims) {
                s.createSimulation();
            }
        }
    }

    private List<String> getXMLFiles() {
        File file = new File("simulator/xml");
        return Lists.newArrayList(file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("xml");
            }
        }));
    }
}
