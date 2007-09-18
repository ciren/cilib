/*
 * CILibHandler.java
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
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.sourceforge.cilib.ioc.registry.ObjectRegistry;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The handler that is provided to the SAX parser to interpret the tags
 * as well as to construct the <tt>Simulation</tt> objects needed.
 * 
 * @author Gary Pampara and Francois Geldenhuys
 */
public class CilibHandler extends DefaultHandler {
	
	private static Logger log = Logger.getLogger(CilibHandler.class);
	private Stack<Object> stack;
	private List<Simulation> simulations;
	
	public CilibHandler() {
		stack = new Stack<Object>();
		simulations = new ArrayList<Simulation>();
	}

	public void setDocumentLocator(Locator locator) {

	}

	public void startDocument() throws SAXException {
		log.debug("Start document");		
	}

	public void endDocument() throws SAXException {
		log.debug("End Document");
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {

	}

	public void endPrefixMapping(String prefix) throws SAXException {
	
	}

	
	/**
	 * Found the start tag for an element.
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param atts
	 * @throws SAXException if an error has occoured within the SAX parser
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if ("".equals (uri)) {
		    log.debug("start element: " + qName);

		    // Get the associated values, if they exist
	    	String id = atts.getValue("id");
	    	String clazz = atts.getValue("class");
	    	String value = atts.getValue("value");
	    	String ref = atts.getValue("ref");
	    	
	    	Object created = null;
	    	
	    	if (clazz != null) {
	    		created = createInstance(clazz); // Create instance
	    		log.debug("Created instance: " + created);
	    		
	    		if (id != null) {
		    		ObjectRegistry.getInstance().addObject(id, created);
		    	}
	    	}
	    	else if (value != null) {
	    		// Create value object
	    		created = createValueObject(value);
	    	}
	    	else if (ref != null) {
	    		log.debug("reference to existing object requested: " + ref);
	    		Object injectedObject = ObjectRegistry.getInstance().getObject(ref);
	    		Object stackTop = stack.peek();
	    		log.debug("Object: " + injectedObject + " injected into object: " + stackTop);
	    		
	    		applyAdditionalProperties(injectedObject, atts);
	    		applyProperty(stackTop, qName, injectedObject);
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
		    log.debug("Start element: {" + uri + "}" + localName);
	}

	/**
	 * The end element has been found for a specific tag.
	 * @param uri
	 * @param localName
	 * @param qName
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("".equals (uri))
		    log.debug("End element: " + qName);
		else
		    log.debug("End element: {" + uri + "}" + localName);
		
		
		Object stackTop = stack.pop();
		
		// Add the stack object to the simulation list iff it is a simulation object
		if (stackTop instanceof Simulation) {
			log.debug("Adding simulation to the list of simulations to execute");
			simulations.add((Simulation) stackTop);
		}
	}

	
	public void characters(char[] ch, int start, int length) throws SAXException {

	}

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		
	}

	public void processingInstruction(String target, String data) throws SAXException {
		
	}

	public void skippedEntity(String name) throws SAXException {
		
	}
	
	
	/**
	 * Create a new instance of the provided class name.
	 * 
	 * @param className The name of the class
	 * @return The newly instanciated class.
	 */
	public Object createInstance(String className) {
		Object result = null;
		
		try {
			Class<?> clazz = Class.forName("net.sourceforge.cilib." + className);
			result = clazz.newInstance();
		}
		catch (ClassNotFoundException c) {
			log.error("Cannot find class [" + className + " for instantiation. Please ensure that the spelling is correct and that the class does exist");
			c.printStackTrace();
		} catch (InstantiationException e) {
			log.error("Cannot instanciate class [" + className + "] the class is most probably abstract or an interface");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("Cannot create instance of class [" + className + "], the access to the required method / constructor is not accessible from the public namespace");
			e.printStackTrace();
		}
		
		return result;
	}
	

	/**
	 * Create the value object based on the provided {@see java.lang.String}.
	 * Conversion to the appropriate type is attempted before the the default
	 * case of a {@see java.lang.String} is returned.
	 * 
	 * @param value The value to be converted to the appropriate type.
	 * @return The converted type as an <tt>Object</tt>.
	 */
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
	

	/**
	 * Apply the additional properties to the specified object. These attributes
	 * are additional to the main attribute values.
	 * 
	 * @param created The instantiated object to have the attributes applied to.
	 * @param atts The attributes to be applied.
	 */
	private void applyAdditionalProperties(Object created, Attributes atts) {
		for (int i = 0; i < atts.getLength(); i++) {
			String attributeName = atts.getQName(i);
			
			if (!attributeName.equals("class") && 
				!attributeName.equals("id") && 
				!attributeName.equals("ref") && 
				!attributeName.equals("value")) {
				log.debug("Applying attribute(" + attributeName + ") with value(" + atts.getValue(i) + ") to object(" + created +")");
				applyProperty(created, attributeName, createValueObject(atts.getValue(i)));
			}
		}
	}
	
	
	/**
	 * Apply the given object to the current object via a setter mutator.
	 * 
	 * @param object The object to apply the setter mutator to.
	 * @param propertyName The name of the setter mutator to call.
	 * @param value The value of the argument to be passed to the setter mutator.
	 */
	private void applyProperty(Object object, String propertyName, Object value) {
		boolean executed = false;
		String propertySetName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		
		try {
			invokeMethod(propertySetName, object, value);
			executed = true;
		} 
		catch (NoSuchMethodException e) {
			// Intentionally do nothing here... the check later handles the invocation if this one fails
		}
		
		if (!executed) {
			try {
				invokeMethod(propertyName, object, value);
			} 
			catch (NoSuchMethodException e) {
				log.error("No method with name: " + propertySetName + " or " + propertyName + " was found when trying to apply the value (" + value + ") on object: " + object.toString());
				e.printStackTrace();
			}			
		}
	}
	
	
	/**
	 * Invoke the required method <tt>propertyName</tt> on the given object <tt>object</tt> with the
	 * given value <tt>value</tt>. The <tt>value</tt> object is converted into the appropriate type
	 * and applied to the method as an argument.
	 *  
	 * @param propertyName The name of the methof to call.
	 * @param object The object to perform the invocation on.
	 * @param value The argument to be applied to the object via the invocation method.
	 * @throws NoSuchMethodException if the needed method is not found within <tt>object</tt>.
	 */
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
			log.error("A security exception was thrown. The call does not have permission to execute.");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			log.error("An invalid / illegal argument was passed to the method invocation. " + object.toString() + "." + propertyName + "(" + value + ") is an illegal arguement");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("Cannot invoke method: " + propertyName + "with argument(s): " + value + " because the access modifiers do not allow it. The access to the method should be public.");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("An exception occoured during the invocation of the method: " + propertyName + ". The cause of this error is :" + e.getCause().getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Perform the lookup of the required method starting at the given object. If the method
	 * is not found, continue the lookup based on the super class and the interfaces of the
	 * object. The search in turn is continued until the end of the inheritance hierarchy is
	 * reached or the method is found. 
	 * 
	 * @param object The object to search for the required method
	 * @param propertyName The required method to look for.
	 * @param clazz The class of the argument to be passed to the method.
	 * @return The <code>Method</code> of the required method call.
	 */
	private Method perfromLookupOfSuperClassAndSuperInterfaces(Object object, String propertyName, Class<?> clazz) {
		
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
	
	
	
	/**
	 * Get the generated simulations.
	 * @return
	 */
	public List<Simulation> getSimulations() {
		return this.simulations;
	}

}
