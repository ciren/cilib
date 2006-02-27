/*
 * Simulator.java
 *
 * Created on February 4, 2003, 3:59 PM
 *
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
 *   
 */

package net.sourceforge.cilib.Simulator;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.cilib.XML.XMLAlgorithmFactory;
import net.sourceforge.cilib.XML.XMLObjectFactory;
import net.sourceforge.cilib.XML.XMLProblemFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This is the entry point for the CILib simulator. This class accepts one
 * command line parameter, which is the name of the XML config file to parse.
 *
 * @author  espeer
 */
public class Main {
    
    /** Creates a new instance of Simulator */
    private Main(Document config) {
        this.config = config;
        simulations = config.getElementsByTagName("simulation");
        frame = new ProgressFrame(simulations.getLength());
        frame.show();
    }
    
    private void runSimulations() {
        for (int i = 0; i < simulations.getLength(); ++i) {
            frame.setSimulation(i);
            Element current = (Element) simulations.item(i);
            XMLAlgorithmFactory algorithmFactory = new XMLAlgorithmFactory(config, (Element) current.getElementsByTagName("algorithm").item(0));
            XMLProblemFactory problemFactory = new XMLProblemFactory(config, (Element) current.getElementsByTagName("problem").item(0));
            XMLObjectFactory measurementsFactory = new XMLObjectFactory(config, (Element) current.getElementsByTagName("measurements").item(0));
            MeasurementSuite suite = (MeasurementSuite) measurementsFactory.newObject();
            Simulation simulation = new Simulation(algorithmFactory, problemFactory, suite);
            simulation.addProgressListener(frame);
            simulation.run();
            simulation = null;
            System.gc();
        }
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: Simulator <simulation-config.xml>");
            System.exit(1);
        }
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // dbf.setValidating(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(args[0]));
        
        Main simulator = new Main(doc);
        simulator.runSimulations(); 
        System.exit(0);
    }
    
    private Document config;
    private NodeList simulations;
    private ProgressFrame frame;
}
