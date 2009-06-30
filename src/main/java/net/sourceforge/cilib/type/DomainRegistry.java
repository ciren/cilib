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
package net.sourceforge.cilib.type;

import java.io.Serializable;

import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.Cloneable;

/**
 *
 * @author gpampara
 *
 */
public interface DomainRegistry extends Cloneable, Serializable {

    /**
     * {@inheritDoc}
     */
    public DomainRegistry getClone();

    /**
     * Set the value of the string representing the domain.
     * @param domainString The domainString to set.
     */
    public void setDomainString(String domainString);

    /**
     * Get the string specifying the domain.
     * @return Returns the domainString.
     */
    public String getDomainString();

    /**
     * Get the string representing the domain, after if has been expanded to
     * a dimensional string with a descriptve component for each dimension.
     * @return Returns the expandedRepresentation.
     */
//    public String getExpandedRepresentation();

    /**
     * Get the instance of the built representation for this domain string.
     * @return Returns the builtRepresenation.
     */
    public StructuredType getBuiltRepresenation();

    /**
     * Get the dimension of the built representation of the domain string.
     * @return The dimension of the domain string.
     */
    public int getDimension();

}

