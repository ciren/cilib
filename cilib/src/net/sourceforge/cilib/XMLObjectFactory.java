/*
 * XMLObjectFactory.java
 *
 * Created on January 15, 2003, 12:38 PM
 *
 * 
 * Copyright (C) 2003 - Edwin S. Peer
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

package net.sourceforge.cilib;

import org.w3c.dom.*;

import java.lang.*;
import java.lang.reflect.*;
import java.util.*;

import net.sourceforge.cilib.Algorithm.*;
import net.sourceforge.cilib.Problem.*;

/**
 *
 * @author  espeer
 */
public class XMLObjectFactory implements AlgorithmFactory, ProblemFactory {
    
    public XMLObjectFactory(Element xmlObjectDescription) {
        this.xmlDocument = null;
        this.xmlObjectDescription = xmlObjectDescription;
    }
    
    public XMLObjectFactory(Document xmlDocument, Element xmlObjectDescription) {
        this.xmlDocument = xmlDocument;
        this.xmlObjectDescription = xmlObjectDescription;
    }
        
    public Algorithm newAlgorithm() {
        return (Algorithm) newObject(xmlObjectDescription);
    }
    
    public Problem newProblem() {
        return (Problem) newObject(xmlObjectDescription);
    }

    public Object newObject() {
        return newObject(xmlObjectDescription);
    }

    private Object newObject(Element xml) {
        
        String className = getClassName(xml);
        
        if (className.equals(this.getClass().getName())) {
            Object[] parameters = {xmlDocument, getFirstChildElement(xml)};
            Class[] types = {Document.class, Element.class};
            try {
                return this.getClass().getConstructor(types).newInstance(parameters);
            }
            catch (Exception ex) {
                error(xml, "Could not create new factory");
            }
        }
        
        Object object = instanciate(xml, className);
       
        setup(object, xml);
        
        return object;
    }

    private Element getReferencedElement(Element xml) {
        if (xmlDocument == null) {
            error(xml, "Can't search for idref without Document");
        }
        Element xmlRef = xmlDocument.getElementById(xml.getAttribute("idref"));
        if (xmlRef == null) {
            error(xml, "Invalid idref " + xml.getAttribute("idref") + ", no matching id found");
        }
        return xmlRef;
    }
    
    private String getClassName(Element xml) {
        // Determine the class to instanciate
        String className = "";
        if (xml.hasAttribute("idref")) {
            className = getReferencedElement(xml).getAttribute("class");
        }
        if (xml.hasAttribute("class")) {
            className = xml.getAttribute("class");
        }
        if (className.length() == 0) {
            error(xml, "No class specified");
        }
        
        // Class names may be relative to this package, absolute classes must be prefixed with a period
        try {
            if (className.charAt(0) == '.') {
                className = className.substring(1);   
            }
            else {
                Package pkg = this.getClass().getPackage();
                if (pkg != null) {
                    className = pkg.getName() + "." + className;
                }
            }           
        }
        catch (IndexOutOfBoundsException ex) {
            error(xml, "Invalid class " + className);
        }
        
        return className;
    }
    
    private Object instanciate(Element xml, String className) {
        // Find the class
        Class objectClass = null;
        try {
            objectClass = Class.forName(className);
        }
        catch (ClassNotFoundException ex) {
            error(xml, "Could not find class " + className);
        }
        
        // Attempt to instanciate the class
        Object object = null;
        try {
            object = objectClass.newInstance();
        }
        catch (Exception ex) {
            error(xml, "Could not instanciate " + objectClass.getName());
        }
        
        return object;
    }
    
    private void setup(Object object, Element xml) {        
        // handle any referenced element
        if (xml.hasAttribute("idref")) {
            setup(object, getReferencedElement(xml));
        }

        // handle attributes of current element
        NamedNodeMap attributes = xml.getAttributes();
        for (int i = 0; i < attributes.getLength(); ++i) {
            Attr attribute = (Attr) attributes.item(i);
            if (attribute.getName().equals("id") || attribute.getName().equals("idref") || attribute.getName().equals("class") || attribute.getName().equals("value")) {
                continue;
            }
            invokeSetMethod(xml, object, attribute.getName(), newObject(attribute.getValue()));
        }
        
        // handle sub-elements of current element
        for (Element e = getFirstChildElement(xml); e != null; e = getNextSiblingElement(e)) {
            if (e.hasAttribute("value")) {
                invokeSetMethod(e, object, e.getTagName(), newObject(e.getAttribute("value")));
            }
            else if (e.hasAttribute("class") || e.hasAttribute("idref")) {
                invokeAnyMethod(e, object, e.getTagName(), newObject(e));
            }
            else if (getFirstChildElement(e) == null) {
                Text text = getFirstChildText(e);
                if (text == null) {
                    error(e, "Can't create object from null text");
                }
                invokeAnyMethod(e, object, e.getTagName(), newObject(text.getNodeValue()));
            }
            else {
                ArrayList parameters = new ArrayList();
                for (Element ee = getFirstChildElement(e); ee != null; ee= getNextSiblingElement(ee)) {
                    if (ee.hasAttribute("value")) {
                        parameters.add(newObject(ee.getAttribute("value")));
                    }
                    else if (ee.hasAttribute("class") || ee.hasAttribute("idref")) {
                        parameters.add(newObject(ee));
                    }
                    else {
                        Text text = getFirstChildText(ee);
                        if (text == null) {
                            error(ee, "Can't create object from null text");
                        }
                        parameters.add(newObject(text.getNodeValue()));
                    }        
                }
                invokeMethod(e, object, e.getTagName(), parameters.toArray());
            }
        }
    }
   
    
    private Element getNextSiblingElement(Node current) {
        current = current.getNextSibling();
        while (current != null && current.getNodeType() != Node.ELEMENT_NODE) {
            current = current.getNextSibling();
        }
        return (Element) current;
    }
    
    private Element getFirstChildElement(Node current) {
        current = current.getFirstChild();
        while (current != null && current.getNodeType() != Node.ELEMENT_NODE) {
            current = current.getNextSibling();
        }
        return (Element) current;
    }
    
    private Text getFirstChildText(Node current) {
        current = current.getFirstChild();
        while (current != null && current.getNodeType() != Node.TEXT_NODE) {
            current = current.getNextSibling();
        }
        return (Text) current;
    }
    
    private Object newObject(String value) {
        try { 
            Integer i = Integer.valueOf(value);
            return i;
        }
        catch (NumberFormatException e) { }
        try {
            Long l = Long.valueOf(value);
            return l;
        }
        catch (NumberFormatException e) { }
        try {
            Double d = Double.valueOf(value);
            return d;
        }
        catch (NumberFormatException e) { }
        if (value.compareToIgnoreCase("true") == 0 || value.compareToIgnoreCase("false") == 0) {
            Boolean b = Boolean.valueOf(value);
            return b;
        }
        else {
            return value;
        }
    }

    private void invokeAnyMethod(Element xml, Object target, String name, Object value) {
        try {
            invokeSetMethod(xml, target, name, value);
        }
        catch (FactoryException ex) { 
            Object[] parameter = { value };
            invokeMethod(xml, target, name, parameter);
        }
    }
    
    private void invokeSetMethod(Element xml, Object target, String property, Object value) {
        String setMethodName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
        Object[] parameters = { value };
        invokeMethod(xml, target, setMethodName, parameters);
    }
    
    private String getParameterString(Object[] parameters) {
        boolean comma = false;
        String parameterString = "";
        for (int i = 0; i < parameters.length; ++i) {
            if (comma) {
                parameterString += ", ";
            }
            parameterString += parameters[i].getClass().getName();
            comma = true;
        }        
        return parameterString;
    }
    
    private void invokeMethod(Element xml, Object target, String methodName, Object[] parameters) {
        Method method = null;
        
        // Find the method
        Method[] methods = target.getClass().getMethods();
        for (int i = 0; i < methods.length; ++i) {
            if (methods[i].getName().equals(methodName)) {
                if (parameters.length != methods[i].getParameterTypes().length) {
                    continue;
                }

                boolean match = true;
                for (int j = 0; j < parameters.length; ++j) {
                    if (methods[i].getParameterTypes()[j].isPrimitive()) {
                        Class type = methods[i].getParameterTypes()[j];
                        if (parameters[j] instanceof Integer && ! (type.equals(Integer.TYPE) || type.equals(Long.TYPE) || type.equals(Double.TYPE))) {
                            match = false;
                        }
                        else if (parameters[j] instanceof Long && ! (type.equals(Long.TYPE) || type.equals(Double.TYPE))) {
                            match = false;
                        }
                        else if (parameters[j] instanceof Double && ! type.equals(Double.TYPE)) {
                            match = false;
                        }
                        else if (parameters[j] instanceof Boolean && ! type.equals(Boolean.TYPE)) {
                            match = false;
                        }
                    }
                    else if (! methods[i].getParameterTypes()[j].isInstance(parameters[j])) {
                        match = false;
                    }
                }
                if (match) {
                    method = methods[i];
                    break;
                }
            }
        }
        
        if (method == null) {
            error(xml, target.getClass().getName() + " does not expose a " + methodName + "(" + getParameterString(parameters) + ") method");            
        }
        
        try {
            method.invoke(target, parameters);
        }
        catch (InvocationTargetException ex) {
           error(xml, "Invoking " + target.getClass().getName() + "." + method.getName() + "(" + getParameterString(parameters) + ") caused: " + ex.getTargetException().toString());
        }
        catch (Exception ex) {
           error(xml, "Could not invoke " + target.getClass().getName() + "." + method.getName() + "(" + getParameterString(parameters) + ")");
        }
    }

    private void error(Element element, String message) {
        throw new FactoryException("In <" + element.getTagName() + "> : " + message); 
    }
    
    private Document xmlDocument;
    private Element xmlObjectDescription;
}