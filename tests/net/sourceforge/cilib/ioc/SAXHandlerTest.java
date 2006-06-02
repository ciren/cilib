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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class SAXHandlerTest {
	
	private static ByteArrayInputStream bis;
	private static CilibHandler handler;
	private static XMLReader reader;
	
	@BeforeClass
	public static void setup() {
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<?xml version='1.0' encoding='utf-8'?>");
		xmlBuffer.append("<simulator>");
		xmlBuffer.append("<algorithms>");
		xmlBuffer.append("  <algorithm id='gbest' class='pso.PSO'>");
		xmlBuffer.append("    <addStoppingCondition class='stoppingcondition.MaximumIterations' iterations='1000' />");
		xmlBuffer.append("  </algorithm>");
		xmlBuffer.append("</algorithms>");
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
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void getOutput() throws SAXException, IOException {
		reader.parse(new InputSource(bis));
	}
	
	
}
