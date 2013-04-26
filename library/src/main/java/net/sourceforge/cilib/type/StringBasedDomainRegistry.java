/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type;

import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Class to perform the needed mappings between a top level domain string
 * and the built representation.
 *
 */
public class StringBasedDomainRegistry implements DomainRegistry {
    private static final long serialVersionUID = 3821361290684036030L;
    private String domainString;
    private StructuredType builtRepresenation;


    /**
     * Construct an instance of the DomainRegistry that will contain the needed
     * information about the domain.
     */
    public StringBasedDomainRegistry() {
    }

    /**
     * Copy Constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public StringBasedDomainRegistry(StringBasedDomainRegistry copy) {
        this.domainString = copy.domainString;
        if (this.builtRepresenation != null)
            this.builtRepresenation = copy.builtRepresenation.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringBasedDomainRegistry getClone() {
        return new StringBasedDomainRegistry(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomainString() {
        return domainString;
    }

    /**
     * Set the value of the string representing the domain.
     * @param domainString The domainString to set.
     */
    @Override
    public void setDomainString(String domainString) {
        this.domainString = domainString;
        this.builtRepresenation = net.sourceforge.cilib.type.parser.DomainParser.parse(domainString);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StructuredType getBuiltRepresentation() {
        return this.builtRepresenation;
    }

    /**
     * Set the representation for this domain string. This may cause an inconsistency
     * as the built representation and the domain string may differ, depending on the
     * values of the objects.
     * @param builtRepresenation The builtRepresenation to set.
     */
    public void setBuiltRepresenation(StructuredType builtRepresenation) {
        this.builtRepresenation = builtRepresenation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        return this.builtRepresenation.size();
    }

}

