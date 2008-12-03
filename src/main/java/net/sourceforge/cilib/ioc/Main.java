/*
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
package net.sourceforge.cilib.ioc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


import net.sourceforge.cilib.simulator.SimulationException;

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
		handler = new CilibHandler();
		FileReader fileReader = null;
		
		try {
			fileReader = new FileReader(filename);
			reader = XMLReaderFactory.createXMLReader();
			
			reader.setContentHandler(handler);
			reader.setErrorHandler(handler);
			reader.parse(new InputSource(fileReader));
		} 
		catch (FileNotFoundException e) {
			System.err.println("The XML configuration file: " + filename + " was not found. Please ensure that the file exists that the specified path");
			e.printStackTrace();
		} 
		catch (SAXException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
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
		
		if (simulations.size() == 0)
			throw new SimulationException("Zero (0) simulations could be found.\nPlease ensure that the XML file is correct, as no simulation objects could be constructed.");
		
		for (Simulation simulation : simulations) {
			simulation.initialise();
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
