/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.simulator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

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
        final String usage = "Please provide the correct arguments.\nUsage: Simulator <simulation-config.xml> | cb <template.xml> <output.xml>";

        if (args.length != 1 && args.length != 3) {
            System.out.println(usage);
            System.exit(1);
        }
        // call generator instead if |params| == 3
        if(args.length == 3){
        	if(args[0].equalsIgnoreCase("cb")){
        		generate(args[1],args[2]);
        	} else {
                System.out.println(usage);
                System.exit(1);
            }
        } else {
        	final List<Simulator> simulators = SimulatorShell.prepare(new File(args[0]));
            ProgressText progress = new ProgressText(simulators.size());

            SimulatorShell.execute(simulators, progress);
        }
    }
    
    // Call generator with cmd args
    private static void generate(String templateDir, String outputDir){
    	
    	try {
			new CombinatorialGenerator(templateDir,outputDir).generate();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}
