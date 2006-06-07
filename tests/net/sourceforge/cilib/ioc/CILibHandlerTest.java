/*
 * SAXParserTest.java
 * 
 * Created on Jun 2, 2006
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
package net.sourceforge.cilib.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.functions.continuous.Spherical;
import net.sourceforge.cilib.ioc.registry.ObjectRegistry;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.simulator.MeasurementSuite;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class CILibHandlerTest {
	
	private static ByteArrayInputStream bis;
	private static CilibHandler handler;
	private static XMLReader reader;
	
	@BeforeClass
	public static void setup() {
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<?xml version='1.0' encoding='utf-8'?>");
		xmlBuffer.append("<!DOCTYPE simulator [");
		xmlBuffer.append("<!ELEMENT simulator (algorithms?, problems?, simulation+)>");
		xmlBuffer.append("<!ELEMENT algorithms>");
		xmlBuffer.append("<!ELEMENT simulation (algorithm,problem,measurements)>");
		xmlBuffer.append("<!ELEMENT algorithm (addStoppingCondition+)>");
		xmlBuffer.append("<!ATTLIST simulation class CDATA 'ioc.Simulation'>");
		xmlBuffer.append("]>");
		xmlBuffer.append("<simulator>");
		xmlBuffer.append("<algorithms>");
		xmlBuffer.append("  <algorithm id='gbest' class='pso.PSO'>");
		xmlBuffer.append("    <addStoppingCondition class='stoppingcondition.MaximumIterations' iterations='1000' />");
		xmlBuffer.append("  </algorithm>");
		xmlBuffer.append("</algorithms>");
		xmlBuffer.append("<problems>");
		xmlBuffer.append("  <problem id='spherical' class='problem.FunctionMinimisationProblem'>");
		xmlBuffer.append("    <function class='functions.continuous.Spherical' />");
		xmlBuffer.append("  </problem>");
		xmlBuffer.append("</problems>");
		xmlBuffer.append("<measurements id='measurements' class='simulator.MeasurementSuite' resolution='10' samples='1'>");
		xmlBuffer.append("  <addMeasurement class='measurement.single.Fitness' />");
		xmlBuffer.append("</measurements>");
		xmlBuffer.append("<simulation class='ioc.Simulation'>");
		xmlBuffer.append("  <algorithm ref='gbest' />");
		xmlBuffer.append("  <problem ref='spherical' />");
		xmlBuffer.append("  <measurements ref='measurements' />");
		xmlBuffer.append("</simulation>");
		xmlBuffer.append("</simulator>");
		
		try {
			bis = new ByteArrayInputStream(xmlBuffer.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		handler = new CilibHandler();
		
		try {
			reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(handler);
			reader.setErrorHandler(handler);
			reader.setFeature("http://xml.org/sax/features/validation", true);
			
			reader.parse(new InputSource(bis));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void validateObjectRegistry() {
		assertEquals(3, ObjectRegistry.getInstance().size());
		
		PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) ObjectRegistry.getInstance().getObject("gbest");
		OptimisationProblem problem = (OptimisationProblem) ObjectRegistry.getInstance().getObject("spherical");
		MeasurementSuite suite = (MeasurementSuite) ObjectRegistry.getInstance().getObject("measurements");
		
		assertNotNull(algorithm);
		assertNotNull(problem);
		assertNotNull(suite);
	}
	
	@Test
	public void initialiseAlgorithm() {
		try {
			PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) ObjectRegistry.getInstance().getObject("gbest");
			OptimisationProblem problem = (OptimisationProblem) ObjectRegistry.getInstance().getObject("spherical");
			
			algorithm.setOptimisationProblem(problem);
			
			algorithm.initialise();
		}
		catch (Exception e) {
			fail("Initialisation of algorithm failed.");
		}
	}
	
	@Test
	public void testBuiltObjectStructure() {
		PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) ObjectRegistry.getInstance().getObject("gbest");
		List<StoppingCondition> stoppingConditions = algorithm.getStoppingConditions();
		assertEquals(1, stoppingConditions.size());
		assertTrue(stoppingConditions.get(0) instanceof MaximumIterations);
		
		MaximumIterations iterations = (MaximumIterations) stoppingConditions.get(0);
		assertEquals(1000, iterations.getIterations());
				
		OptimisationProblem problem = (OptimisationProblem) ObjectRegistry.getInstance().getObject("spherical");
		assertTrue(problem instanceof FunctionMinimisationProblem);
		FunctionMinimisationProblem fmp = (FunctionMinimisationProblem) problem;
		assertTrue(fmp.getFunction() instanceof Spherical);
		
		MeasurementSuite suite = (MeasurementSuite) ObjectRegistry.getInstance().getObject("measurements");
		assertEquals(10, suite.getResolution());
		assertEquals(1, suite.getSamples());
	}
	
	
}
