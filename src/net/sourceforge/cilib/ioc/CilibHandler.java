/*
 * XMLParser.java
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

import net.sourceforge.cilib.ioc.registry.ServiceManager;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CilibHandler extends DefaultHandler {
	
	private Stack<Object> stack;
	
	public CilibHandler() {
		stack = new Stack();
	}

	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	public void startDocument() throws SAXException {
		System.out.println("Start document");		
	}

	public void endDocument() throws SAXException {
		System.out.println("End document");
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if ("".equals (uri)) {
		    System.out.println("Start element: " + qName);

		    // Get the associated values, if they exist
	    	String id = atts.getValue("id");
	    	String clazz = atts.getValue("class");
	    	String value = atts.getValue("value");
	    	
	    	Object created = null;
	    	
	    	if (clazz != null) {
	    		created = createInstance(clazz); // Create instance
	    		System.out.println("Created instance: " + created);
	    		
	    		if (id != null) {
		    		ServiceManager.getInstance().addObject(id, created);
		    	}
	    	}
	    	else if (value != null) {
	    		// Create value object
	    		created = createValueObject(value);
	    	}
	    	
	    	if (created != null) {
	    		// Call extra mutators for all remaining attributes
	    		applyAdditionalProperties(created, atts);
	    		
	    		Object stackTop = stack.peek();
	    		
	    		if (!(stackTop instanceof String)) {
	    			// Perfrom setter mutator method call
	    			applyProperty(stackTop, qName, created);
	    		}
	    		
	    		stack.push(created);
	    	}
	    	else { // Put a placeholder on the stack
	    		stack.push(qName);
	    	}
		}
		else
		    System.out.println("Start element: {" + uri + "}" + localName);
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("".equals (uri))
		    System.out.println("End element: " + qName);
		else
		    System.out.println("End element: {" + uri + "}" + localName);
		
		
		stack.pop();		
	}

	
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void processingInstruction(String target, String data) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * 
	 * @param className
	 * @return
	 */
	public Object createInstance(String className) {
		Object result = null;
		
		try {
			Class<?> clazz = Class.forName("net.sourceforge.cilib." + className);
			result = clazz.newInstance();
		}
		catch (ClassNotFoundException c) {
			c.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public Object createValueObject(String value) {
		try {
			return Integer.valueOf(value);
		}
		catch (NumberFormatException integerException) {}
		
		try {
			return Long.valueOf(value);
		}
		catch (NumberFormatException longException) {}
		
		try {
			return Double.valueOf(value);
		}
		catch (NumberFormatException doubleException) {}
		
		if (value.equalsIgnoreCase("true"))
			return new Boolean(true);
		
		if (value.equalsIgnoreCase("false"))
			return new Boolean(false);
		
		return new String(value);
	}
	

	private void applyAdditionalProperties(Object created, Attributes atts) {
		for (int i = 0; i < atts.getLength(); i++) {
			String attributeName = atts.getQName(i);
			
			if (!attributeName.equals("class") && !attributeName.equals("id")) {
				System.out.println("Applying: " + attributeName);
				applyProperty(created, attributeName, createValueObject(atts.getValue(i)));
			}
		}
	}
	
	
	private void applyProperty(Object object, String propertyName, Object value) {
		boolean executed = false;
		String propertySetName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		
		try {
			invokeMethod(propertySetName, object, value);
			executed = true;
		} catch (NoSuchMethodException e) {
			//e.printStackTrace();
		}
		
		if (!executed) {
			try {
				invokeMethod(propertyName, object, value);
			} catch (NoSuchMethodException e) {
				System.err.println("No method with name: " + propertySetName + " or " + propertyName + " was found.");
				e.printStackTrace();
			}			
		}
	}
	
	private void invokeMethod(String propertyName, Object object, Object value) throws NoSuchMethodException {
		Method method = null;
		try {
			
			try {
				method = object.getClass().getMethod(propertyName, value.getClass());
			} catch (NoSuchMethodException e1) {
				if (value instanceof Integer) {
					method = object.getClass().getMethod(propertyName, Integer.TYPE);
				}
				else if (value instanceof Long) {
					method = object.getClass().getMethod(propertyName, Long.TYPE);
				}
				else if (value instanceof Double) {
					method = object.getClass().getMethod(propertyName, Double.TYPE);
				}
				else {
					method = perfromLookupOfSuperClassAndSuperInterfaces(object, propertyName, value.getClass());
				
					if (method == null)
						throw e1;
				}
			}
				
			method.invoke(object, value);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	private Method perfromLookupOfSuperClassAndSuperInterfaces(Object object, String propertyName, Class clazz) {
		
		if (clazz == null)
			return null;
		
		Method method = null;
		
		try {
			method = object.getClass().getMethod(propertyName, clazz);
		}
		catch (NoSuchMethodException e) {		
			Class<?>[] interfaces = clazz.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				method = perfromLookupOfSuperClassAndSuperInterfaces(object, propertyName, interfaces[i]);
				if (method != null)
					break;
			}
			
			if (method == null && !clazz.isInterface())
				method = perfromLookupOfSuperClassAndSuperInterfaces(object, propertyName, clazz.getSuperclass());
		}
		
		return method;
	}

}
