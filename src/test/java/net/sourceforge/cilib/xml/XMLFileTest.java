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
package net.sourceforge.cilib.xml;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sourceforge.cilib.simulator.MeasurementSuite;
import net.sourceforge.cilib.simulator.ProgressListener;
import net.sourceforge.cilib.simulator.ProgressText;
import net.sourceforge.cilib.simulator.Simulator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML related tests.
 */
@RunWith(Parameterized.class)
public class XMLFileTest {
    private final String filename;

    public XMLFileTest(String filename) {
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
     * Tests will pass if all the instance creation succeeds.
     * </p>
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    @Test
    public void simulationConstruction() throws ParserConfigurationException, SAXException, IOException {
        System.out.println("Constructing: " + filename);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("xml", filename));

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
            Simulator simulation = new Simulator(algorithmFactory, problemFactory, suite);
            if(progress != null) {
                simulation.addProgressListener(progress);
            }

            simulation = null;
        }
    }

    @Parameterized.Parameters
    public static List<Object[]> getXMLFiles() {
        File file = new File("xml");
        List<String> files = Arrays.asList(file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("xml"))
                    return true;

                return false;
            }
        }));

        return Lists.transform(files, new Function<String, Object[]>() {
            @Override
            public Object[] apply(String from) {
                return new Object[]{ from };
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
