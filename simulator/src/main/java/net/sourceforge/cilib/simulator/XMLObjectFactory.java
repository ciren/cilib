/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.simulator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.w3c.dom.*;

/**
 * <p>
 * The <code>XMLObjectFactory</code> can be used to manage the construction of any object
 * based on an XML description. This class uses reflection to set properties
 * and call arbitrary methods of the object under construction.
 * </p>
 * <p>
 * Example:
 * </p>
 * <pre>
 *   // Parse the XML document
 *   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
 *   DocumentBuilder db = dbf.newDocumentBuilder();
 *   Document doc = db.parse(new File("example.xml"));
 *
 *   // Here, construct is the XML element to read the configuration from
 *   Element description = (Element) doc.getElementsByTagName("construct").item(0);
 *
 *   // The document is only necessary for idrefs, there is a
 *   // second constructor that accepts just the element
 *   XMLObjectFactory factory = new XMLObjectFactory(doc, description);
 *
 *   // Finally, call newObject() whenever you want to construct a new
 *   // object according to the configuration in the given XML element
 *   Object object = factory.newObject();
 * </pre>
 *
 * <p>
 * Each time <code>XMLObjectFactory</code> encounters a class attribute in the XML, an
 * instance of the specified class is instantiated. Nested elements are
 * interpreted as either properties to be set or methods to be called on this
 * newly constructed object. If no class attribute is specified then the element
 * is interpreted as a Java primitive type or a string. Elements can be nested in
 * a hierarchical fashion to handle the construction of complex objects.
 * </p>
 * <p>
 * Note: Requires the Java API for XML processing (JAXP).
 * </p>
 *
 * See: <a href="http://www.sourceforge.net/projects/cilib/">Factory Demo</a> for XML samples.
 *
 */
public class XMLObjectFactory {

    private Document xmlDocument;
    private Element xmlObjectDescription;

    /**
     * Creates a new instance of <code>XMLObjectFactory</code> for constructing objects
     * given an XML description and an XML document for handling idrefs.
     *
     * @param xmlObjectDescription An XML element that describes the objects to be constructed by this factory.
     * @param xmlDocument The XML document to search for any idrefs.
     **/
    public XMLObjectFactory(Document xmlDocument, Element xmlObjectDescription) {
        this.xmlDocument = xmlDocument;
        this.xmlObjectDescription = xmlObjectDescription;
    }

    /**
     * Constructs a new {@link java.lang.Object} based on the underlying XML object description.
     *
     * @exception FactoryException In case the object cannot be constructed.
     * @return A new {@link java.lang.Object} constructed according to the given description.
     */
    public Object newObject() {
        return newObject(xmlObjectDescription);
    }

    @SuppressWarnings("unchecked")
    private Object newObject(Element xml) {
        Class<?> objectClass = getClass(xml);
        Object object = instanciate(xml, objectClass);
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

    private Class<?> getClass(Element xml) {
        // Determine the class to instantiate

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

        try {
            return Class.forName("net.sourceforge.cilib." + className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: net.sourceforge.cilib." + className);
            e.printStackTrace();
        }

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
            e.printStackTrace();
        }

        error(xml, "Class not found: " + className);
        return null;
    }

    private Object instanciate(Element xml, Class<?> objectClass) {
        // Attempt to instantiate the class
        Object object = null;
        try {
            object = objectClass.newInstance();
        } catch (Exception ex) {
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
            } else if (e.hasAttribute("class") || e.hasAttribute("idref")) {
                invokeAnyMethod(e, object, e.getTagName(), newObject(e));
            } else if (getFirstChildElement(e) == null) {
                Text text = getFirstChildText(e);
                if (text == null) {
                    error(e, "Can't create object from null text");
                }
                invokeAnyMethod(e, object, e.getTagName(), newObject(text.getNodeValue()));
            } else {
                ArrayList<Object> parameters = new ArrayList<Object>();
                for (Element ee = getFirstChildElement(e); ee != null; ee = getNextSiblingElement(ee)) {
                    if (ee.hasAttribute("value")) {
                        parameters.add(newObject(ee.getAttribute("value")));
                    } else if (ee.hasAttribute("class") || ee.hasAttribute("idref")) {
                        parameters.add(newObject(ee));
                    } else {
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
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
        }
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException e) {
        }
        try {
            return Double.valueOf(value.trim());
        } catch (NumberFormatException e) {
        }
        if (value.trim().compareToIgnoreCase("true") == 0 || value.trim().compareToIgnoreCase("false") == 0) {
            return Boolean.valueOf(value);
        } else {
            return value;
        }
    }

    private void invokeAnyMethod(Element xml, Object target, String name, Object value) {
        try {
            invokeSetMethod(xml, target, name, value);
        } catch (Exception ex) {
            Object[] parameter = {value};
            invokeMethod(xml, target, name, parameter);
        }
    }

    private void invokeSetMethod(Element xml, Object target, String property, Object value) {
        String setMethodName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
        Object[] parameters = {value};
        invokeMethod(xml, target, setMethodName, parameters);
    }

    private String getParameterString(Object[] parameters) {
        StringBuilder builder = new StringBuilder();
        boolean comma = false;
        for (int i = 0; i < parameters.length; ++i) {
            if (comma) {
                builder.append(", ");
            }
            builder.append(parameters[i].getClass().getName());
            comma = true;
        }
        return builder.toString();
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
                        Class<?> type = methods[i].getParameterTypes()[j];
                        if (parameters[j] instanceof Integer && !(type.equals(Integer.TYPE) || type.equals(Long.TYPE) || type.equals(Double.TYPE))) {
                            match = false;
                        } else if (parameters[j] instanceof Long && !(type.equals(Long.TYPE) || type.equals(Double.TYPE))) {
                            match = false;
                        } else if (parameters[j] instanceof Double && !type.equals(Double.TYPE)) {
                            match = false;
                        } else if (parameters[j] instanceof Boolean && !type.equals(Boolean.TYPE)) {
                            match = false;
                        }
                    } else if (!methods[i].getParameterTypes()[j].isInstance(parameters[j])) {
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
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            error(xml, "Invoking " + target.getClass().getName() + "." + method.getName() + "(" + getParameterString(parameters) + ") caused: " + ex.getTargetException().toString());
        } catch (Exception ex) {
            error(xml, "Could not invoke " + target.getClass().getName() + "." + method.getName() + "(" + getParameterString(parameters) + ")");
        }
    }

    protected void error(Element element, String message) {
        throw new RuntimeException("In <" + element.getTagName() + "> : " + message);
    }
}
