/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.simulator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.google.common.collect.Lists;

public class CombinatorialGenerator {

	String outputDir;
	Document config;

	public CombinatorialGenerator(String templateDir, String outputDir)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		config = db.parse(new File(templateDir));
		this.outputDir = outputDir;
	}

	public void generate() throws TransformerException {

		NodeList simElemList = config.getElementsByTagName("simulation");

		List<ComboSimulation> simulations = new ArrayList<ComboSimulation>();
		Map<String, SimulationComponent> components = new HashMap<String, SimulationComponent>();

		// create combinatorial simulations and
		// components(algorithms,problems,measurements)
		String compID;
		SimulationComponent component;
		ComboSimulation simulation;
		Element simElement, compElement, compRefElement, outputElem;
		String[] compTypes = { "algorithm", "problem", "measurements" };
		for (int sim = 0; sim < simElemList.getLength(); ++sim) {
			simElement = (Element) simElemList.item(sim);
			outputElem = (Element) simElement.getElementsByTagName("output")
					.item(0);
			simulation = new ComboSimulation(xmlToString(simElement),
					outputElem.getAttribute("file").toString(), outputElem
							.getAttribute("format").toString());

			for (String type : compTypes) {
				compRefElement = (Element) simElement
						.getElementsByTagName(type).item(0);
				compID = compRefElement.getAttribute("idref").toString();
				if (components.containsKey(compID)) {
					component = components.get(compID);
				} else {
					compElement = getReferencedElement(compRefElement, config);
					component = buildElement(compElement, null, components);
				}
				simulation.addComponent(type, component);
			}

			// build simulations
			simulation.combos = recursiveCombination(
					simulation.getComponentListMap(), 0);
			simulations.add(simulation);
		}

		// replace original component & simulation defs with combinatorial defs
		String configXML = xmlToString(config);
		// components
		for (SimulationComponent comp : components.values()) {
			configXML = configXML.replaceFirst(Pattern.quote(comp.originalXML),
					comp.getAllCombos());
		}
		// simulations
		for (ComboSimulation sim : simulations) {
			configXML = configXML.replaceFirst(Pattern.quote(sim.xml),
					sim.getAllCombos());
		}

		// write out generated-combinatorial config
		stringToFile(outputDir, getConfigHeader() + configXML);
		// System.out.println(configXML);

	}

	protected SimulationComponent buildElement(Element compElement,
			SimulationComponent parent, Map<String, SimulationComponent> comps)
			throws TransformerException {

		// if parent is passed then we assume we are not creating a simComponent
		// for the passed element
		// otherwise we build the passed element
		SimulationComponent ret = parent;
		String retID = null;
		if (parent == null) {
			retID = compElement.getAttribute("id").toString();
			String xml = xmlToString(compElement);
			ret = getCTags(retID, xml);
		}
		String childID;
		for (Element e = getFirstChildElement(compElement); e != null; e = getNextSiblingElement(e)) {
			if (e.hasAttribute("idref")) {
				childID = e.getAttribute("idref").toString();
				if (!comps.containsKey(childID)) {
					ret.addChild(
							childID,
							buildElement(getReferencedElement(e, config), null,
									comps));
				}
			}
			if (e.hasChildNodes()) {
				buildElement(e, ret, comps);
			}
		}

		// only do this on the level that "ret" was created
		// when parent != null ret == parent which is created in a higher
		// buildElement() call
		if (parent == null) {
			ret.combos = recursiveCombination(ret.getAttributesList(), 0);
			comps.put(retID, ret);
		}

		return ret;
	}

	protected SimulationComponent getCTags(String compID, String orig) {

		String xml = orig;
		Map<String, List<String>> attributeVals = new HashMap<String, List<String>>();
		int start = 0, endbrace, end;
		while (true) {
			start = xml.indexOf("@C", start);
			if (start < 0) {
				break;
			}
			endbrace = xml.indexOf("}", start);
			end = xml.indexOf("\"", endbrace);
			attributeVals.put(xml.substring(endbrace + 1, end),
					Lists.newArrayList(xml.substring(start + 3, endbrace)
							.split(",")));
			// System.out.println( attributeVals );
			xml = xml.replaceFirst(
					Pattern.quote(xml.substring(start + 2, endbrace + 1)), "");
			start = end;
		}

		// System.out.println(xml);
		return new SimulationComponent(compID, xml, orig, attributeVals);
	}

	protected List<Map<String, String>> recursiveCombination(
			List<Entry<String, List<String>>> valList, int listIndex) {

		if (listIndex >= valList.size()) {
			List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
			ret.add(new HashMap<String, String>());
			return ret;
		}

		String attKey = valList.get(listIndex).getKey();
		List<String> attValues = valList.get(listIndex).getValue();

		// System.out.println(attKey+" "+attValues.size());

		List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
		List<Map<String, String>> temp;
		for (String val : attValues) {
			// System.out.println(tabString(listIndex)+val);
			temp = recursiveCombination(valList, listIndex + 1);
			for (Map<String, String> map : temp) {
				map.put(attKey, val);
			}
			ret.addAll(temp);
		}

		return ret;

	}

	// -----------------------------------------------------------+
	// xml methods
	protected static Element getReferencedElement(Element xml, Document xmlDoc) {
		if (xmlDoc == null) {
			error(xml, "Can't search for idref without Document");
		}
		Element xmlRef = xmlDoc.getElementById(xml.getAttribute("idref"));
		if (xmlRef == null) {
			error(xml, "Invalid idref " + xml.getAttribute("idref")
					+ ", no matching id found");
		}
		return xmlRef;
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

	protected static String xmlToString(Node xmlNode)
			throws TransformerException {

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(xmlNode), new StreamResult(writer));
		// return writer.getBuffer().toString().replaceAll("\n|\r", "");
		return writer.getBuffer().toString();
	}

	protected static void error(Element element, String message) {
		throw new RuntimeException("In <" + element.getTagName() + "> : "
				+ message);
	}

	// -----------------------------------------------------------+
	// write methods
	protected static void writeConfig(String path, Node xmlNode)
			throws TransformerException {

		stringToFile(path, getConfigHeader() + xmlToString(xmlNode));
	}

	protected static String getConfigHeader() {

		return "<?xml version=\"1.0\"?>\n" + "<!DOCTYPE simulator [\n"
				+ "<!ATTLIST algorithm id ID #IMPLIED>\n"
				+ "<!ATTLIST problem id ID #IMPLIED>\n"
				+ "<!ATTLIST measurements id ID #IMPLIED>\n" + "]>\n";
	}

	protected static void stringToFile(String path, String string) {

		// System.out.println(string);
		try {
			File newTextFile = new File(path);
			if (newTextFile.getParentFile() != null) {
				newTextFile.getParentFile().mkdirs();
			}

			FileWriter fw = new FileWriter(newTextFile);
			fw.write(string);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -----------------------------------------------------------+
	private class SimulationComponent {

		// idref in xml, used for naming
		String id;

		// xml with attribute vals stripped, attribute name left for later
		// replacement
		String xml, originalXML;

		// <attributeName,[list of values]>
		Map<String, List<String>> attributeVals;

		// [list of all combinations of <attributeName,value>]
		List<Map<String, String>> combos;

		// child components - allows components to be recursively defined
		Map<String, SimulationComponent> childComponents;

		public SimulationComponent(String id, String xml, String original,
				Map<String, List<String>> attributeVals) {

			this.id = id;
			this.xml = xml;
			this.originalXML = original;
			this.attributeVals = attributeVals;
			this.childComponents = new HashMap<String, SimulationComponent>();
		}

		public void addChild(String childID, SimulationComponent child) {

			attributeVals.put(childID, child.getAllSignatures());
			childComponents.put(childID, child);
		}

		public List<Entry<String, List<String>>> getAttributesList() {

			return Lists.newArrayList(attributeVals.entrySet());
		}

		public int comboCount() {

			return (combos == null || combos.size() < 1) ? 1 : combos.size();
		}

		public String getCombo(int index) {

			if (combos == null || combos.size() < 1) {
				return xml;
			}
			Map<String, String> combo = combos.get(index);
			String ret = xml;

			for (Entry<String, String> att : combo.entrySet()) {
				if (childComponents.containsKey(att.getKey())) {
					ret = ret.replace(att.getKey(), att.getValue());
				} else {
					ret = ret.replace("@C" + att.getKey(), att.getValue());
				}
			}
			ret = ret.replaceFirst("id=\"" + id + "\"", "id=\""
					+ getComboSignature(index) + "\"");

			return ret;
		}

		public String getComboSignature(int index) {

			if (combos == null || combos.size() < 1) {
				return id;
			}
			Map<String, String> combo = combos.get(index);
			String ret = id + "_";

			for (Entry<String, String> att : combo.entrySet()) {
				if (childComponents.containsKey(att.getKey())) {
					ret += "(" + att.getValue() + ")_";
				} else {
					ret += att.getKey() + "(" + att.getValue() + ")_";
				}

			}

			return ret.substring(0, ret.length() - 1);
		}

		public List<String> getAllSignatures() {

			List<String> ret = new ArrayList<String>();

			for (int i = 0; i < combos.size(); i++) {
				ret.add(getComboSignature(i));
			}

			return ret;
		}

		public String getAllCombos() {

			String ret = "";
			for (int i = 0; i < comboCount(); ++i) {
				ret += getCombo(i) + "\n\n";
			}
			return ret;
		}
	}

	private class ComboSimulation {

		// output value, used for naming output Files
		String output, format;

		// xml of simulation, will be replaced with component combinations
		String xml;

		// <componentType,[listOfComponents]>
		Map<String, SimulationComponent> componentVals;

		// [list of all combinations of <componentType,ComponentIdRef>]
		List<Map<String, String>> combos;

		public ComboSimulation(String xml, String output, String format) {

			this.xml = xml;
			this.output = output;
			this.format = format.toLowerCase();
			componentVals = new HashMap<String, SimulationComponent>();
		}

		public void addComponent(String compType, SimulationComponent component) {

			componentVals.put(compType, component);
		}

		public List<Entry<String, List<String>>> getComponentListMap() {

			List<Entry<String, List<String>>> ret = new ArrayList<Entry<String, List<String>>>();
			for (Entry<String, SimulationComponent> entry : componentVals
					.entrySet()) {
				ret.add(new AbstractMap.SimpleEntry<String, List<String>>(entry
						.getKey(), entry.getValue().getAllSignatures()));
			}

			return ret;
		}

		public int comboCount() {
			return (combos == null || combos.size() < 1) ? 1 : combos.size();
		}

		public String getCombo(int index) {

			if (combos == null || combos.size() < 1) {
				return xml;
			}
			Map<String, String> combo = combos.get(index);
			String ret = xml;

			SimulationComponent comp;
			for (Entry<String, String> att : combo.entrySet()) {
				comp = componentVals.get(att.getKey());
				ret = ret.replaceFirst(comp.id, att.getValue());
			}
			ret = ret.replaceFirst(output, output + getComboSignature(index)
					+ "." + format);

			return ret;
		}

		public String getComboSignature(int index) {

			if (combos == null || combos.size() < 1) {
				return "";
			}
			Map<String, String> combo = combos.get(index);
			String ret = "";

			for (Entry<String, String> att : combo.entrySet()) {
				ret += "[" + att.getValue() + "]";
			}

			return ret;
		}

		public String getAllCombos() {

			String ret = "";
			for (int i = 0; i < comboCount(); ++i) {
				ret += getCombo(i) + "\n\n";
			}
			return ret;
		}
	}

	// -----------------------------------------------------------+

}
