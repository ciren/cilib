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

import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.stoppingcondition.StoppingConditionFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author  Edwin Peer
 */
public class XMLStoppingConditionFactory extends XMLObjectFactory implements StoppingConditionFactory {

    public XMLStoppingConditionFactory() {
        super(null);
    }

    /** Creates a new instance of XMLProgressIndicatorFactory. */
    public XMLStoppingConditionFactory(Document xmlDocument, Element xmlProgressIndicatorDescription) {
        super(xmlDocument, xmlProgressIndicatorDescription);
        if (!xmlProgressIndicatorDescription.getTagName().equals("progressIndicator")) {
            error(xmlProgressIndicatorDescription, "Expected <progressIndicator> tag");
        }
    }

    public XMLStoppingConditionFactory(Element xmlProgressIndicatorDescription) {
        super(xmlProgressIndicatorDescription);
        if (!xmlProgressIndicatorDescription.getTagName().equals("progressIndicator")) {
            error(xmlProgressIndicatorDescription, "Expected <progressIndicator> tag");
        }
    }

    /** Returns a newly constructed StoppingCondition.
     *
     * @return A new {@link StoppingCondition}.
     *
     */
    public StoppingCondition newStoppingCondition() {
        return (StoppingCondition) newObject();
    }

    public void setProgressIndicator(StoppingCondition progressIndicator) {
        // hack to make CiClops introspector work properly
    }
}
