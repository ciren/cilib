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

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.AlgorithmFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author  Edwin Peer
 */
public class XMLAlgorithmFactory extends XMLObjectFactory implements AlgorithmFactory {

    public XMLAlgorithmFactory() {
        super(null);
    }

    /** Creates a new instance of XMLAlgorithmFactory. */
    public XMLAlgorithmFactory(Document xmlDocument, Element xmlAlgorithmDescription) {
        super(xmlDocument, xmlAlgorithmDescription);
        if (!xmlAlgorithmDescription.getTagName().equals("algorithm")) {
            error(xmlAlgorithmDescription, "Expected <algorithm> tag");
        }
    }

    public XMLAlgorithmFactory(Element xmlAlgorithmDescription) {
        super(xmlAlgorithmDescription);
        if (!xmlAlgorithmDescription.getTagName().equals("algorithm")) {
            error(xmlAlgorithmDescription, "Expected <algorithm> tag");
        }
    }

    /** Returns a newly constructed algorithm.
     *
     * @return A new {@link Algorithm}.
     *
     */
    public Algorithm newAlgorithm() {
        return (Algorithm) newObject();
    }

    public void setAlgorithm(Algorithm algorithm) {
        // hack to make CiClops introspector work properly
    }

}
