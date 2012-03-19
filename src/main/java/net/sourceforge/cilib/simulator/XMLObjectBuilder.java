/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.simulator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 */
class XMLObjectBuilder implements LinkedObjectBuilder, LinkedXMLObjectBuilder {
    private Document config;
    private Element element;

    LinkedObjectBuilder config(final Document config) {
        this.config = config;
        return this;
    }

    @Override
    public LinkedXMLObjectBuilder element(Node item) {
        this.element = (Element) item;
        return this;
    }

    @Override
    public XMLObjectFactory build() {
        return new XMLObjectFactory(config, element);
    }

}
