/**
 * Copyright (C) 2003 - 2008
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

package net.sourceforge.cilib.xml;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sourceforge.cilib.simulator.MeasurementSuite;
import net.sourceforge.cilib.simulator.ProgressListener;
import net.sourceforge.cilib.simulator.ProgressText;
import net.sourceforge.cilib.simulator.Simulation;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML related tests.
 */
public class XMLFileTest {

	/**
	 * <p>
	 * Iterate through all the availble XML simulation specifications and
	 * instantiate the needed objects, as well as making the simulations
	 * ready for execution. After this process, discard the constructed
	 * instances.
	 * </p>
	 * <p>
	 * Tests will pass if all the instance creation succeeds.
	 * </p>
	 */
	@Test
	public void simulationConstruction() throws ParserConfigurationException, SAXException, IOException {
		File file = new File("xml");
		String [] fileList = getXMLFiles(file);

		for (String filename : fileList) {
			System.out.println("Constructing: " + filename);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(file, filename));

			NodeList simulations = doc.getElementsByTagName("simulation");
			ProgressListener progress = new ProgressText(simulations.getLength());
			
			for (int i = 0; i < simulations.getLength(); ++i) {
				if(progress != null)
					progress.setSimulation(i);

				Element current = (Element) simulations.item(i);
				XMLAlgorithmFactory algorithmFactory = new XMLAlgorithmFactory(doc, (Element) current.getElementsByTagName("algorithm").item(0));
				XMLProblemFactory problemFactory = new XMLProblemFactory(doc, (Element) current.getElementsByTagName("problem").item(0));
				XMLObjectFactory measurementsFactory = new XMLObjectFactory(doc, (Element) current.getElementsByTagName("measurements").item(0));
			    MeasurementSuite suite = (MeasurementSuite) measurementsFactory.newObject();
				Simulation simulation = new Simulation(algorithmFactory, problemFactory, suite);
				if(progress != null) {
					simulation.addProgressListener(progress);
				}

				simulation = null;
			}
		}
	}

	private String[] getXMLFiles(File file) {
		return file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith("xml"))
					return true;

				return false;
			}
		});
	}
}
