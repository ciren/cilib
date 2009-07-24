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

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.cilib.xml.XMLAlgorithmFactory;
import net.sourceforge.cilib.xml.XMLObjectFactory;
import net.sourceforge.cilib.xml.XMLProblemFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This is the entry point for the CILib simulator. This class accepts one
 * command line parameter, which is the name of the XML config file to parse.
 *
 * @author  Edwin Peer
 */
public final class Main {

    /** Creates a new instance of Simulator */
    private Main(Document config, ProgressListener progress) {
        this.config = config;
        this.progress = progress;
        simulations = config.getElementsByTagName("simulation");
    }

    private void runSimulations() {
        for (int i = 0; i < simulations.getLength(); ++i) {
            if(progress != null)
                progress.setSimulation(i);
            Element current = (Element) simulations.item(i);
            XMLAlgorithmFactory algorithmFactory = new XMLAlgorithmFactory(config, (Element) current.getElementsByTagName("algorithm").item(0));
            XMLProblemFactory problemFactory = new XMLProblemFactory(config, (Element) current.getElementsByTagName("problem").item(0));
            XMLObjectFactory measurementsFactory = new XMLObjectFactory(config, (Element) current.getElementsByTagName("measurements").item(0));
            MeasurementSuite suite = (MeasurementSuite) measurementsFactory.newObject();
            Simulator simulator = new Simulator(algorithmFactory, problemFactory, suite);
            if(progress != null) {
                simulator.addProgressListener(progress);
            }

            simulator.execute();
            simulator = null;
            System.gc();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: Simulator <simulation-config.xml> [-noprogress|-textprogress|-guiprogress]");
            System.exit(1);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // dbf.setValidating(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(args[0]));

        ProgressListener progress = null;
        if (args.length > 1 && args[1].equals("-textprogress")) {
            progress = new ProgressText(doc.getElementsByTagName("simulation").getLength());
        }
        else if (args.length > 1 && args[1].equals("-guiprogress")) { //-guiprogress
            ProgressFrame pf = new ProgressFrame(doc.getElementsByTagName("simulation").getLength());
            pf.setVisible(true);
            progress = pf;
        }
        else {
            progress = new NoProgress();
        }

        Main simulator = new Main(doc, progress);
        simulator.runSimulations();
        System.exit(0);
    }

    private Document config;
    private NodeList simulations;
    private ProgressListener progress;
}
