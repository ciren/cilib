/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.simulator;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Provider;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
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
     * Iterate through all the availble XML simulation specifications and
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
        SimulatorShell shell = new SimulatorShell(new XMLObjectBuilder(), new SimulatorCreator(new FakeService()), new MeasurementCombinerBuilder());
        shell.prepare(new File("xml", filename));
    }

    @Parameterized.Parameters
    public static List<Object[]> getXMLFiles() {
        File file = new File("xml");
        List<String> files = Arrays.asList(file.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("xml")) {
                    return true;
                }

                return false;
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

    private class FakeService implements Provider<ExecutorService> {

        @Override
        public ExecutorService get() {
            return null;
        }
    }
}
