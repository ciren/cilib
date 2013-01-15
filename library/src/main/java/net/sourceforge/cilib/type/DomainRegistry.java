/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type;

import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.Cloneable;

public interface DomainRegistry extends Cloneable {

    /**
     * {@inheritDoc}
     */
    DomainRegistry getClone();

    /**
     * Set the value of the string representing the domain.
     * @param domainString The domainString to set.
     */
    void setDomainString(String domainString);

    /**
     * Get the string specifying the domain.
     * @return Returns the domainString.
     */
    String getDomainString();

    /**
     * Get the instance of the built representation for this domain string.
     * @return Returns the builtRepresentation.
     */
    StructuredType getBuiltRepresentation();

    /**
     * Get the dimension of the built representation of the domain string.
     * @return The dimension of the domain string.
     */
    int getDimension();

}

