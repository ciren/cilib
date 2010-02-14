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

import com.google.common.base.Preconditions;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.cilib.algorithm.ProgressListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This is the entry point for the CIlib simulator. This class accepts one
 * command line parameter, which is the name of the XML config file to parse.
 *
 * @author  Edwin Peer
 */
public final class Main {
    private final Provider<XMLObjectBuilder> objectBuilderProvider;

    @Inject
    Main(Provider<XMLObjectBuilder> objectBuilderProvider) {
        this.objectBuilderProvider = objectBuilderProvider;
    }

    private void runSimulations(Document config, ProgressListener progress) {
        Preconditions.checkNotNull(progress);

        NodeList simulations = config.getElementsByTagName("simulation");

        for (int i = 0; i < simulations.getLength(); ++i) {
            Element current = (Element) simulations.item(i);

            XMLObjectFactory algorithmFactory = objectBuilderProvider.get().config(config).element(current.getElementsByTagName("algorithm").item(0)).build();
            XMLObjectFactory problemFactory = objectBuilderProvider.get().config(config).element(current.getElementsByTagName("problem").item(0)).build();
            XMLObjectFactory measurementsFactory = objectBuilderProvider.get().config(config).element((Element) current.getElementsByTagName("measurements").item(0)).build();
            MeasurementSuite suite = (MeasurementSuite) measurementsFactory.newObject();

            Simulator simulator = new Simulator(algorithmFactory, problemFactory, suite);
            simulator.addProgressListener(progress);
            progress.setSimulation(i);

            simulator.execute();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: Simulator <simulation-config.xml> [-noprogress|-textprogress|-guiprogress]");
            System.exit(1);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(args[0]));

        ProgressListener progress = null;
        if (args.length > 1 && args[1].equals("-textprogress")) {
            progress = new ProgressText(doc.getElementsByTagName("simulation").getLength());
        } else if (args.length > 1 && args[1].equals("-guiprogress")) { //-guiprogress
            ProgressFrame pf = new ProgressFrame(doc.getElementsByTagName("simulation").getLength());
            pf.setVisible(true);
            progress = pf;
        } else {
            progress = new NoProgress();
        }

        Injector injector = Guice.createInjector(new SimulatorModule());
        Main simulator = injector.getInstance(Main.class);
        simulator.runSimulations(doc, progress);
    }
}
