/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.simulator;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sourceforge.cilib.math.random.generator.seeder.NetworkBasedSeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
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
public class SimulatorShell {
    
    private SimulatorShell() {}
    
    /**
     * Prepare a list of {@code Simulator} instances for execution.
     * @param specification to be read defining the simulations.
     * @return the list of instances to execute.
     */
    public static List<Simulator> prepare(File specification) {
        try {
            List<Simulator> simulators = Lists.newArrayList();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document config = db.parse(specification);

            NodeList simulations = config.getElementsByTagName("simulation");
            for (int i = 0; i < simulations.getLength(); ++i) {
                Element current = (Element) simulations.item(i);
                
                XMLObjectFactory algorithmFactory = new XMLObjectFactory(config, (Element) current.getElementsByTagName("algorithm").item(0));
                XMLObjectFactory problemFactory = new XMLObjectFactory(config, (Element) current.getElementsByTagName("problem").item(0));
                XMLObjectFactory measurementsFactory = new XMLObjectFactory(config, (Element) current.getElementsByTagName("measurements").item(0));
                MeasurementCombiner combiner = createCombiner((Element) current.getElementsByTagName("output").item(0));
                
                int samples = current.hasAttribute("samples") ? Integer.valueOf(current.getAttribute("samples")) : 1;
                
                SeedSelectionStrategy seeder;
                NodeList seeders = current.getElementsByTagName("seeder");
                if (seeders.getLength() >= 1) {
                    XMLObjectFactory seederFactory = new XMLObjectFactory(config, (Element) seeders.item(0));
                    seeder = (SeedSelectionStrategy) seederFactory.newObject();
                } else {
                    seeder = new NetworkBasedSeedSelectionStrategy();
                }

                Simulator simulator = new Simulator(algorithmFactory, problemFactory, measurementsFactory, combiner, samples, seeder);
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
     * @param listener responsible to monitor progress.
     * TODO: This listener idea is not fresh - one listener per simulation should be the case.
     */
    public static void execute(Iterable<Simulator> simulators, ProgressText listener) {
        int index = 0;
        for (Simulator simulator : simulators) {
            simulator.init(); // Prepare the simulator by initialising the simulations
            simulator.addProgressListener(listener);
            listener.setSimulation(index++);
            simulator.execute();
        }
    }

    private static MeasurementCombiner createCombiner(Element item) {
        return new MeasurementCombiner(new File(item.getAttribute("file")));
    }
}
