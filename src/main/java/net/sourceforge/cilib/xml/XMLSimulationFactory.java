/*
 * XMLSimulationFactory.java
 *
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
package net.sourceforge.cilib.xml;

import net.sourceforge.cilib.ciclops.Simulation;
import net.sourceforge.cilib.ciclops.SimulationFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author  Edwin Peer
 */
public class XMLSimulationFactory extends XMLObjectFactory implements SimulationFactory {
    
	public XMLSimulationFactory() {
		super(null);
	}

    /** Creates a new instance of XMLSimulationFactory. */
    public XMLSimulationFactory(Document xmlDocument, Element xmlAlgorithmDescription) {
        super(xmlDocument, xmlAlgorithmDescription);
        if (!xmlAlgorithmDescription.getTagName().equals("simulation")) {
            error(xmlAlgorithmDescription, "Expected <simulation> tag");
        }
    }
    
    public XMLSimulationFactory(Element xmlAlgorithmDescription) {
        super(xmlAlgorithmDescription);
        if (!xmlAlgorithmDescription.getTagName().equals("simulation")) {
            error(xmlAlgorithmDescription, "Expected <simulation> tag");
        }
    }
    
    /** Returns a newly constructed simulation.
     *
     * @return A new {@link Simulation}.
     *
     */
    public Simulation newSimulation() {
        return (Simulation) newObject();
    }  
    
    public void setSimulation(Simulation simulation) {
        // hack to make CiClops introspector work properly 
    }
    
}
