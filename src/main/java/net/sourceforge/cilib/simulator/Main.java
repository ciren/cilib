/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.simulator;

import java.io.File;
import java.util.List;
import net.sourceforge.cilib.algorithm.ProgressListener;

/**
 * This is the entry point for the CIlib simulator. This class accepts one
 * command line parameter, which is the name of the XML config file to parse.
 *
 */
public final class Main {

    private Main() {
        throw new UnsupportedOperationException("Cannot instantiate.");
    }

    /**
     * Main entry point for the simulator.
     * @param args provided arguments.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Please provide the correct arguments.\nUsage: Simulator <simulation-config.xml> [-textprogress|-guiprogress]");
        }

        SimulatorShell shell = new SimulatorShell(new XMLObjectBuilder(), new SimulatorCreator(), new MeasurementCombinerBuilder());
        final List<Simulator> simulators = shell.prepare(new File(args[0]));

        ProgressListener progress = null;
        if (args.length > 1 && args[1].equals("-textprogress")) {
            progress = new ProgressText(simulators.size());
        } else if (args.length > 1 && args[1].equals("-guiprogress")) {
            ProgressFrame pf = new ProgressFrame(simulators.size());
            pf.setVisible(true);
            progress = pf;
        } else {
            progress = new NoProgress();
        }

        shell.execute(simulators, progress);
    }
}
