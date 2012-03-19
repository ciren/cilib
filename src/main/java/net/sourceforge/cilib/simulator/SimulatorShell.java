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
 * The basic skeleton of the simulator. The aim of the shell is to allow
 * for the required instances to be assembled before the simulator instance
 * is actually created and executed.
 * <p>
 * The {@code SimulatorShell} may construct more than one simulator instance
 * if required, based on the provided simulation file.
 *
 */
class SimulatorShell {

    private final XMLObjectBuilder objectBuilder;
    private final SimulatorCreator creator;
    private final MeasurementCombinerBuilder combinerBuilder;

    @Inject
    SimulatorShell(XMLObjectBuilder objectBuilder,
            SimulatorCreator creator,
            MeasurementCombinerBuilder combinerBuilder) {
        this.objectBuilder = objectBuilder;
        this.creator = creator;
        this.combinerBuilder = combinerBuilder;
    }

    /**
     * Prepare a list of {@code Simulator} instances for execution.
     * @param specification to be read defining the simulations.
     * @return the list of instacnes to execute.
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
                MeasurementCombiner combiner = createCombiner((Element) current.getElementsByTagName("output").item(0));

                Simulator simulator = creator.algorithm(algorithmFactory).problem(problemFactory).measurement(measurementsFactory).combiner(combiner).samples(samples).get();
                simulator.init(); // Prepare the simulator by initializing the simulations
                simulators.add(simulator);
            }
            return simulators;
        } catch (Exception ex) {
            throw new RuntimeException("Error preparing: " + specification.getAbsolutePath(), ex);
        }
    }

    /**
     * Run and execute the simulations, reporting progress.
     * @param simulators iterable list to execute.
     * @param listener reposible to monitor progress.
     * TODO: This listener idea is not fresh - one listener per simulation should be the case.
     */
    void execute(Iterable<Simulator> simulators, ProgressListener listener) {
        int index = 0;
        for (Simulator simulator : simulators) {
            simulator.addProgressListener(listener);
            listener.setSimulation(index++);
            simulator.execute();
        }
    }

    /*
     * This shoud be a guice provider.... I don't like this
     */
    private MeasurementCombiner createCombiner(Element item) {
        String file = item.getAttribute("file");
        OutputType format = OutputType.valueOf(item.getAttribute("format").toUpperCase());
        return this.combinerBuilder.build(format, file);
    }
}
