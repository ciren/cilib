package net.sourceforge.cilib.ioc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * 
 * @author Gary Pampara and Francois Geldenhuys
 */
public class Main {
	
	private CilibHandler handler;
	private XMLReader reader;
	
	/**
	 * Constructor.
	 * @param filename
	 */
	public Main(String filename) {
		FileReader fileReader = null;
		
		try {
			fileReader = new FileReader(filename);
			reader = XMLReaderFactory.createXMLReader();
			
			reader.setContentHandler(handler);
			reader.setErrorHandler(handler);
			reader.parse(new InputSource(fileReader));
		} catch (FileNotFoundException e) {
			System.err.println("The XML configuration file: " + filename + " was not found. Please ensure that the file exists that the specified path");
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		runSimulations();
	}
	
	
	/**
	 * 
	 *
	 */
	private void runSimulations() {
		List<Simulation> simulations = handler.getSimulations();
		System.out.println("simulations.size(): " + simulations.size());
		
		for (Simulation simulation : simulations) {
			simulation.run();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main(args[0]);
	}

}
