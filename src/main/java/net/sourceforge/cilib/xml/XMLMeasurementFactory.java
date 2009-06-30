/**
 * Copyright (C) 2003 - 2009
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

import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.measurement.MeasurementFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author  Edwin Peer
 */
public class XMLMeasurementFactory extends XMLObjectFactory implements MeasurementFactory {

    public XMLMeasurementFactory() {
        super(null);
    }

    /** Creates a new instance of XMLMeasurementFactory. */
    public XMLMeasurementFactory(Document xmlDocument, Element xmlMeasurementDescription) {
        super(xmlDocument, xmlMeasurementDescription);
        if (!xmlMeasurementDescription.getTagName().equals("measurement")) {
            error(xmlMeasurementDescription, "Expected <measurement> tag");
        }
    }

    public XMLMeasurementFactory(Element xmlMeasurementDescription) {
        super(xmlMeasurementDescription);
        if (!xmlMeasurementDescription.getTagName().equals("measurement")) {
            error(xmlMeasurementDescription, "Expected <measurement> tag");
        }
    }

    public Measurement newMeasurement() {
        return (Measurement) newObject();
    }

    public void setMeasurement(Measurement measurement) {
        // hack to make CiClops introspector work properly
    }

}
