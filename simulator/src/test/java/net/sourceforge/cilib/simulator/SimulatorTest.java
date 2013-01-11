/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.simulator;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Integration test to ensure that the construction of all provided
 * XML files succeed.
 */
@RunWith(Parameterized.class)
public class SimulatorTest {

    private final String filename;

    public SimulatorTest(String filename) {
        this.filename = filename;
    }

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
        System.out.println("Constructing specification: " + filename);
        List<Simulator> sims = SimulatorShell.prepare(new File("simulator/xml", filename));
        for (Simulator s : sims) {
            s.createSimulation();
        }
    }

    @Parameterized.Parameters
    public static List<Object[]> getXMLFiles() {
        File file = new File("simulator/xml");
        List<String> files = Arrays.asList(file.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("xml");
            }
        }));

        return Lists.transform(files, new Function<String, Object[]>() {

            @Override
            public Object[] apply(String from) {
                return new Object[]{from};
            }

            @Override
            @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
            public boolean equals(Object obj) {
                return super.equals(obj);
            }

            @Override
            public int hashCode() {
                return 0;
            }
        });
    }
}
