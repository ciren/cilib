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

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sourceforge.cilib.algorithm.ProgressListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author gpampara
 */
class SimulatorShell {

    private final XMLObjectBuilder objectBuilder;
    private final SimulatorCreator creator;

    @Inject
    SimulatorShell(XMLObjectBuilder objectBuilder, SimulatorCreator creator) {
        this.objectBuilder = objectBuilder;
        this.creator = creator;
    }

    /**
     *
     * @param specification
     * @return
     */
    List<Simulator> prepare(File specification) {
        try {
            List<Simulator> simulators = Lists.newArrayList();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document config = db.parse(specification);

            NodeList simulations = config.getElementsByTagName("simulation");
            for (int i = 0; i < simulations.getLength(); ++i) {
                Element current = (Element) simulations.item(i);
                int samples = current.hasAttribute("samples") ? Integer.valueOf(current.getAttribute("samples")) : 1;
                XMLObjectFactory algorithmFactory = objectBuilder.config(config).element(current.getElementsByTagName("algorithm").item(0)).build();
                XMLObjectFactory problemFactory = objectBuilder.config(config).element(current.getElementsByTagName("problem").item(0)).build();
                XMLObjectFactory measurementsFactory = objectBuilder.config(config).element((Element) current.getElementsByTagName("measurements").item(0)).build();

                Simulator simulator = creator.algorithm(algorithmFactory).problem(problemFactory).measurement(measurementsFactory).samples(samples).get();
                simulator.init();
                simulators.add(simulator);
            }
            return simulators;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * TODO: This listener idea is not fresh
     * @param simulators
     * @param listener
     */
    void execute(Iterable<Simulator> simulators, ProgressListener listener) {
        int index = 0;
        for (Simulator simulator : simulators) {
            simulator.addProgressListener(listener);
            listener.setSimulation(index++);
            simulator.execute();
        }
    }
}
