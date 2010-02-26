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

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.io.File;
import java.util.List;
import net.sourceforge.cilib.algorithm.ProgressListener;

/**
 * This is the entry point for the CIlib simulator. This class accepts one
 * command line parameter, which is the name of the XML config file to parse.
 *
 * @author  Edwin Peer
 */
public final class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: Simulator <simulation-config.xml> [-noprogress|-textprogress|-guiprogress]");
            System.exit(1);
        }

        Injector injector = Guice.createInjector(new SimulatorModule());
        SimulatorShell shell = injector.getInstance(SimulatorShell.class);
        final List<Simulator> simulators = shell.prepare(new File(args[0]));

        ProgressListener progress = null;
        if (args.length > 1 && args[1].equals("-textprogress")) {
            progress = new ProgressText(simulators.size());
        } else if (args.length > 1 && args[1].equals("-guiprogress")) { //-guiprogress
            ProgressFrame pf = new ProgressFrame(simulators.size());
            pf.setVisible(true);
            progress = pf;
        } else {
            progress = new NoProgress();
        }

        shell.execute(simulators, progress);
    }
}
